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

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
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
		return this.userService.signIn(user);
	}

	@Override
	@PostMapping(value = "/refresh-token", produces = "application/json")
	public ResponseFormat getTokenByRefreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		return this.userService.refreshToken(refreshToken);
	}

	@Override
	@PostMapping(value = "/update", produces = "application/json")
	public ResponseFormat updateUserData(@RequestBody UserUpdateRequest userUpdate) {
		return this.userService.updateUserData(userUpdate);
	}





}
