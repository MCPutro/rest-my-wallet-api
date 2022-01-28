package com.mywallet.api.controller;

import java.util.Optional;

import com.mywallet.api.response.RefreshTokenResponse;
import com.mywallet.api.service.RefreshTokenService;
import com.mywallet.api.service.UserService;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.api.config.jwt.JwtTokenUtil;
import com.mywallet.api.model.UserDetailImp;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.response.UserSignInResponse;
import com.mywallet.api.service.RefreshTokenServiceImpl;
import com.mywallet.api.service.JwtUserDetailsService;
import com.mywallet.api.documentation.UserApi;
import com.mywallet.api.request.RefreshTokenRequest;
import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.request.UserUpdateRequest;

import com.google.gson.JsonObject;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController implements UserApi {

	private final UserService userService;

	private final JwtTokenUtil jwtTokenUtil;

	private final AuthenticationManager authenticationManager;

	private final JwtUserDetailsService userDetailsService;

	private final RefreshTokenService refreshTokenService;

	@Autowired
	public UserController(UserService userService, JwtTokenUtil jwtTokenUtil,
						  AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService,
						  RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.refreshTokenService = refreshTokenService;
	}

	@ApiOperation(value = "", hidden = true)
	@GetMapping("/")
	public String onAir() {
		return "OK";
	}

	@Override
	@PostMapping(value = "/createAndLogin" , produces = "application/json")
	public ResponseFormat createAndLogin(@RequestBody UserSignUpRequest newUser) {
		String temp = newUser.getPassword();
		
		ResponseFormat create = this.userService.insertUser(newUser);
		
		if (create.getStatus() == ResponseFormat.Status.success){
			
			UserSignInRequest firstLogin = UserSignInRequest.builder()
					.email(newUser.getEmail())
					.password(temp)
					.build();

			return this.signIn(firstLogin);
		}else {
			return ResponseFormat.builder()
					.status(create.getStatus())
					.message(create.getMessage())
					.build();

		}

	}

	@Override
	@PostMapping("/signup")
	public ResponseFormat signUp(@RequestBody UserSignUpRequest newUser) {
		return this.userService.insertUser(newUser);
	}

	@Override
	@PostMapping(value = "/signin", produces = "application/json")
	public ResponseFormat signIn(@RequestBody UserSignInRequest user) {
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
	@PostMapping(value = "/refresh-token", produces = "application/json")
	public ResponseFormat getTokenByRefreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		try {

			Optional<com.mywallet.api.entity.RefreshToken> s = this.refreshTokenService.findByToken(refreshToken.getRefreshToken().split("\\.")[0]);

			
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

	@Override
	@PostMapping(value = "/update", produces = "application/json")
	public ResponseFormat updateUserData(@RequestBody UserUpdateRequest userUpdate) {
		return this.userService.updateUserData(userUpdate);
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
