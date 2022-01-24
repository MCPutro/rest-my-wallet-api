package com.mywallet.api.service;

import java.time.LocalDateTime;
//import java.time.Instant;
//import java.util.UUID;

import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
//import com.mywallet.api.repository.RefreshTokenRepository;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.response.UserResponse;

@Service
public class UserService {

	private UserRepository userRepository;

	private FireBaseService firebaseService;

	private PasswordEncoder encoder;

	@Autowired
	public UserService(UserRepository userRepository, FireBaseService firebaseService, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.firebaseService = firebaseService;
		this.encoder = encoder;
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
			User existing = this.userRepository.findByUid(user.getUid());
			return existing;
		}catch (Exception e) {
			return null;
		}
	}
	
	public User getUserByEmail(User user) {
		//Resp resp;
		try {
			User existing = this.userRepository.findByEmail(user.getEmail());
			
			return existing;
		}catch (Exception e) {
			return null;
		}
	}	
	
	@Transactional
	public ResponseFormat updateUserData(UserUpdateRequest newUser) {
		try {
			
			String result = this.firebaseService.update(newUser);
			
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
	
	
	
}
