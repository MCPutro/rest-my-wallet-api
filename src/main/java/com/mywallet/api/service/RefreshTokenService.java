package com.mywallet.api.service;

import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
import com.mywallet.api.request.UserSignInRequest;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    String generateRefresToken(UserSignInRequest user);
    User getUserFromRefreshToken(String token);
    boolean isExpired(RefreshToken token);
}
