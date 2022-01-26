package com.mywallet.api.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.mywallet.api.request.UserSignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
import com.mywallet.api.repository.RefreshTokenRepository;
import com.mywallet.api.repository.UserRepository;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

	private final UserRepository userRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private final Environment env;

	@Autowired
	public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, Environment env) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.env = env;
	}

	public Optional<RefreshToken> findByToken(String token) {
		return this.refreshTokenRepository.findByToken(token);
	}

	@Transactional
	public String generateRefresToken(UserSignInRequest user) {
		try {
			User existing = this.userRepository.findByEmail(user.getEmail());

			if (existing != null) {
				if (existing.getRefreshToken() != null) {
					existing.getRefreshToken().setToken(UUID.randomUUID().toString());
					existing.getRefreshToken().setExpiryDate(Instant.now().plusMillis(Long.parseLong(this.env.getProperty("jwt-key.refreshExp"))));
					this.refreshTokenRepository.save(existing.getRefreshToken());
					
					Date d = Date.from(existing.getRefreshToken().getExpiryDate());
					return existing.getRefreshToken().getToken()+"."+Base64.getEncoder().encodeToString(new String(d.getTime()/1000 + "").getBytes());
				}else {
					RefreshToken rt = new RefreshToken();
					rt.setToken(UUID.randomUUID().toString());
					rt.setExpiryDate(Instant.now().plusMillis(Long.parseLong(this.env.getProperty("jwt-key.refreshExp"))));
					rt.setUser(existing);
					this.refreshTokenRepository.save(rt);
					
					Date d = Date.from(rt.getExpiryDate());
					return rt.getToken()+"."+Base64.getEncoder().encodeToString(new String(d.getTime()/1000 + "").getBytes());
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public User getUserFromRefreshToken(String token) {
		try {
			RefreshToken rt = this.refreshTokenRepository.findByToken(token).orElse(null);
			if (rt != null) {
				return rt.getUser();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public boolean isExpired(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			System.out.println("dihapus1");
			this.refreshTokenRepository.deleteByToken(token.getToken());
			System.out.println("dihapus2");
			return true;
		}
		
		return false;
	}
	
}
