package com.mywallet.api.documentation;


import com.mywallet.api.request.RefreshTokenRequest;
import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.request.UserUpdateRequest;
import com.mywallet.api.response.format.ResponseFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

@Api
public interface UserApi {

    @ApiOperation(value = "Create than login with new account", response = ResponseEntity.class)
    public ResponseEntity<?> createAndLogin(UserSignUpRequest user);

    @ApiOperation(value = "Create new account", response = ResponseFormat.class)
    public ResponseFormat signup(UserSignUpRequest newUser);

    @ApiOperation(value = "Sign in", response = ResponseFormat.class)
    public ResponseFormat signin(UserSignInRequest user);

    @ApiOperation(value = "Refresh token", response = ResponseEntity.class)
    public ResponseEntity<?> getTokenByRefreshToken( RefreshTokenRequest rt) ;

    @ApiOperation(value = "Update info user", response = ResponseFormat.class)
    public ResponseFormat updateUserData( UserUpdateRequest user);

}
