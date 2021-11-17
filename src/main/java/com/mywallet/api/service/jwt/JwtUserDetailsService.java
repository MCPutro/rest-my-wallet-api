package com.mywallet.api.service.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mywallet.api.entity.User;
import com.mywallet.api.model.UserDetailImp;
import com.mywallet.api.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User existing = this.userRepository.findByEmail(username);
		if (existing == null) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
		
		return new org.springframework.security.core.userdetails.User(existing.getEmail(), existing.getPassword(),
				new ArrayList<>());
	}
	
	public UserDetailImp loadUserByEmail(String email) throws UsernameNotFoundException {
		User existing = this.userRepository.findByEmail(email);
		if (existing == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		return new UserDetailImp( existing.getPassword(), existing.getEmail(), existing.getUid(), existing.getUsername());
	}

}
