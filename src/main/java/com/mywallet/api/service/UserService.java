package com.mywallet.api.service;

import java.time.LocalDateTime;
//import java.time.Instant;
//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
//import com.mywallet.api.repository.RefreshTokenRepository;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.response.Resp;
import com.mywallet.api.response.UserResponse;

@Service
public class UserService {

	@Autowired private UserRepository userRepository;
	
	@Autowired private FireBaseService firebaseService;
	
	@Autowired private PasswordEncoder encoder;
	
	@Transactional
	public Resp insertUser(User user) {
		try {
			Resp resp;
			User existing = this.userRepository.findByEmail(user.getEmail());
			
			if (existing == null) {
				user.setRegistrationDate(LocalDateTime.now());
				Resp r = this.firebaseService.createUser(user);

				if (r.getStatus() == Resp.Status.success) {
					user.setUid(((UserResponse) r.getData()).getUid());
					user.setPassword(encoder.encode(user.getPassword()));
					user.setUrlAvatar(((UserResponse) r.getData()).getUrlAvatar());
					User result = this.userRepository.save(user);
					
					resp = Resp.builder()
							.status(Resp.Status.success)
							.data(new UserResponse(result.getUid(), result.getUsername(), result.getEmail(), result.getUrlAvatar() ))
							.build();

				} else {
					resp = Resp.builder()
							.status(Resp.Status.error)
							.message(r.getMessage())
							.build();
				}
			} else {
				resp =  Resp.builder()
						.status(Resp.Status.error)
						.message("Email already registered")
						.data(new UserResponse(null, existing.getUsername(), existing.getEmail(), null))
						.build();
			}
			return resp;
		} catch (Exception e) {
			System.out.println("error : "+e.getMessage());
			return Resp.builder().status(Resp.Status.error).message(e.getMessage()).build();
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
	public Resp updateUserData(User newUser) {
		try {
			
			String result = this.firebaseService.update(newUser);
			
			if (result != null) {
//				return new Resp("error1", result);
				return Resp.builder().status(Resp.Status.error).message(result).build();
			}
			
			User existing = this.userRepository.findByUid(newUser.getUid());
			
			//System.out.println("?? "+existing);
			
			existing.setUsername(newUser.getUsername());
			existing.setEmail(newUser.getEmail());
			existing.setPassword(encoder.encode(newUser.getPassword()));
			existing.setUrlAvatar(newUser.getUrlAvatar());
			
			this.userRepository.save(existing);

			return Resp.builder().status(Resp.Status.success).build();
		} catch (Exception e) {
			return Resp.builder().status(Resp.Status.error).message(e.getMessage()).build();
		}
	}
	
	
	
}
