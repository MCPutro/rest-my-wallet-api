package com.mywallet.api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
import com.mywallet.api.repository.RefreshTokenRepository;
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
		Resp resp;
		try {
			User existing = this.userRepository.findByEmail(user.getEmail());
			
			if (existing == null) {
				user.setRegistrationDate(LocalDateTime.now());
				Resp r = this.firebaseService.createUser(user);
				
				if (r.getStatus().equalsIgnoreCase("success")) {
					user.setUid(((UserResponse) r.getData()).getUid());
					user.setPassword(encoder.encode(user.getPassword()));
					User result = this.userRepository.save(user);
					
					resp = new Resp("success", null, new UserResponse(result.getUid(), result.getUsername(), result.getEmail()));	
				}else {
					resp = new Resp("error", r.getMessage());	
				}

			}else {
				resp = new Resp("error", "Email already registered", new UserResponse(null, existing.getUsername(), existing.getEmail()));	
			}
			
			return resp;
			 
		} catch (Exception e) {
			System.out.println("error : "+e.getMessage());
			resp = new Resp("error", e.getMessage(), null);
			return resp;
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
	
	
}
