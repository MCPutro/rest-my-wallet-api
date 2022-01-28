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

    @ApiOperation(value = "Create than login with new account", response = ResponseFormat.class, nickname = "createAndLogin")
    public ResponseFormat createAndLogin(UserSignUpRequest user);

    @ApiOperation(value = "Sign up", response = ResponseFormat.class, nickname = "signUp")
    public ResponseFormat signUp(UserSignUpRequest newUser);

    @ApiOperation(value = "Sign in", response = ResponseFormat.class, nickname = "signIn")
    public ResponseFormat signIn(UserSignInRequest user);

    @ApiOperation(value = "Refresh token", response = ResponseFormat.class, nickname = "getTokenByRefreshToken")
    public ResponseFormat getTokenByRefreshToken( RefreshTokenRequest rt) ;

    @ApiOperation(value = "Update info user", response = ResponseFormat.class, nickname = "updateUserData")
    public ResponseFormat updateUserData( UserUpdateRequest user);

}
