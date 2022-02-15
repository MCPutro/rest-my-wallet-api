package com.mywallet.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.mywallet.api.config.jwt.JwtTokenUtil;
import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.model.UserDetailImp;
import com.mywallet.api.request.RefreshTokenRequest;
import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.request.UserUpdateRequest;
import com.mywallet.api.response.RefreshTokenResponse;
import com.mywallet.api.response.UserSignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mywallet.api.entity.User;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.response.UserResponse;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	private final FireBaseService firebaseService;

	private final PasswordEncoder encoder;

	private final JwtUserDetailsService userDetailsService;

	private final JwtTokenUtil jwtTokenUtil;

	private final AuthenticationManager authenticationManager;

	private final RefreshTokenService refreshTokenService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, FireBaseService firebaseService, PasswordEncoder encoder, JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.firebaseService = firebaseService;
		this.encoder = encoder;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.authenticationManager = authenticationManager;
		this.refreshTokenService = refreshTokenService;
	}

	@Transactional
	public ResponseFormat insertUser(UserSignUpRequest newUser) {
		try {
			ResponseFormat resp;
			User user = this.userRepository.findByEmail(newUser.getEmail());
			
			if (user == null) {
				user = new User(newUser.getEmail(), newUser.getPassword(), newUser.getUsername(), null, null);
				user.setDeviceId(newUser.getDeviceId());
				user.setRegistrationDate(LocalDateTime.now());

				ResponseFormat r = this.firebaseService.createUser(user);

				if (r.getStatus() == ResponseFormat.Status.success) {
					user.setUid(((UserResponse) r.getData()).getUid());
					user.setPassword(encoder.encode(user.getPassword()));
					user.setUrlAvatar(((UserResponse) r.getData()).getUrlAvatar());
					User result = this.userRepository.save(user);
					
					resp = ResponseFormat.builder()
							.status(ResponseFormat.Status.success)
							.data(new UserResponse(result.getUid(), result.getUsername(), result.getEmail(), result.getUrlAvatar() ))
							.build();

				} else {
					resp = ResponseFormat.builder()
							.status(ResponseFormat.Status.error)
							.message(r.getMessage())
							.build();
				}
			} else {
				resp =  ResponseFormat.builder()
						.status(ResponseFormat.Status.error)
						.message("Email already registered")
						.data(new UserResponse(null, user.getUsername(), user.getEmail(), null))
						.build();
			}
			return resp;
		} catch (Exception e) {
			System.out.println("error : "+e.getMessage());
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}
	
	public User getUserById(User user) {
		//Resp resp;
		try {
			return this.userRepository.findByUid(user.getUid());
		}catch (Exception e) {
			return null;
		}
	}
	
	public User getUserByEmail(User user) {
		//Resp resp;
		try {
			return this.userRepository.findByEmail(user.getEmail());
		}catch (Exception e) {
			return null;
		}
	}	
	
	@Transactional
	public ResponseFormat updateUserData(UserUpdateRequest newUser) {
		try {
			
			String result = this.firebaseService.updateUserInfo(newUser);
			
			if (result != null) {
//				return new Resp("error1", result);
				return ResponseFormat.builder().status(ResponseFormat.Status.error).message(result).build();
			}
			
			User existing = this.userRepository.findByUid(newUser.getUid());
			
			//System.out.println("?? "+existing);
			
			existing.setUsername(newUser.getUsername());
			existing.setEmail(newUser.getEmail());
			existing.setPassword(encoder.encode(newUser.getPassword()));
			existing.setUrlAvatar(newUser.getUrlAvatar());
			
			this.userRepository.save(existing);

			return ResponseFormat.builder().status(ResponseFormat.Status.success).build();
		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}

	@Override
	public ResponseFormat signIn(UserSignInRequest user) {

		try {
			authenticate(user.getEmail(), user.getPassword());

			// UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
			UserDetailImp userDetails = userDetailsService.loadUserByEmail(user.getEmail());

			String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseFormat.builder().status(ResponseFormat.Status.success)
					.data(
							new UserSignInResponse(
									userDetails.getUid(),
									userDetails.getDisplayName(),
									"Bearer",
									token,
									this.refreshTokenService.generateRefresToken(user),
									userDetails.getUrlAvatar()
							)
					)
					.build();

		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error)
					.message(e.getMessage())
					.build();
		}
	}

	@Override
	public ResponseFormat refreshToken(RefreshTokenRequest refreshToken) {
		try {
			Optional<RefreshToken> s = this.refreshTokenService.findByToken(refreshToken.getRefreshToken().split("\\.")[0]);

			if (!s.isPresent()) {
				return ResponseFormat.builder().status(ResponseFormat.Status.error)
						.message("refresh-token '"+refreshToken.getRefreshToken()+ "' is not found")
						.build();
			} else {
				if (!this.refreshTokenService.isExpired(s.get())) {
					String token = this.jwtTokenUtil.generateTokenFromEmail(s.get().getUser().getEmail());

					RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
							.token(token)
							.refreshToken(refreshToken.getRefreshToken())
							.build();

					return ResponseFormat.builder()
							.status(ResponseFormat.Status.success)
							.data(refreshTokenResponse)
							.build();
				}
				else {
					return ResponseFormat.builder().status(ResponseFormat.Status.error)
							.message("refresh-token '"+refreshToken.getRefreshToken()+"' has expired. Please sign in")
							.build();
				}
			}

		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("USER DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID CREDENTIALS", e);
		}
	}


}
