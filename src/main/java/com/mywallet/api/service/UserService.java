package com.mywallet.api.service;

import com.mywallet.api.entity.User;
import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.request.UserUpdateRequest;
import com.mywallet.api.response.format.ResponseFormat;

public interface UserService {
    public ResponseFormat insertUser(UserSignUpRequest newUser);
    public User getUserById(User user) ;
    public User getUserByEmail(User user);
    public ResponseFormat updateUserData(UserUpdateRequest newUser);
}
