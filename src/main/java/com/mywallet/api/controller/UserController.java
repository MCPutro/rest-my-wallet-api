package com.mywallet.api.controller;

import java.util.Optional;

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

	private final JwtTokenUtil jwtTokenUtil;

	private final AuthenticationManager authenticationManager;

	private final JwtUserDetailsService userDetailsService;

	private final RefreshTokenService refreshTokenService;

	@Autowired
	public UserController(UserService userService, JwtTokenUtil jwtTokenUtil,
						  AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService,
						  RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.refreshTokenService = refreshTokenService;
	}

	@ApiOperation(value = "", hidden = true)
	@GetMapping("/")
	public String onAir() {
		return "OK";
	}

	@Override
	@PostMapping(value = "/createAndLogin" , produces = "application/json")
	public ResponseEntity<?> createAndLogin(@RequestBody UserSignUpRequest newUser) {
		String temp = newUser.getPassword();
		JsonObject resp = new JsonObject();
		
		ResponseFormat create = this.userService.insertUser(newUser);
		
		if (create.getStatus() == ResponseFormat.Status.success){
			
			UserSignInRequest firstLogin = UserSignInRequest.builder()
					.email(newUser.getEmail())
					.password(temp)
					.build();
			
			ResponseFormat p = this.signin(firstLogin);
			
			return ResponseEntity.ok(p);
		}else {
			resp.addProperty("status", create.getStatus().toString());
			resp.addProperty("message", create.getMessage());
			return ResponseEntity.ok(resp.toString());
		}
			
		//return null;
	}

	@Override
	@PostMapping("/signup")
	public ResponseFormat signup(@RequestBody UserSignUpRequest newUser) {
		return this.userService.insertUser(newUser);
	}

	@Override
	@PostMapping(value = "/signin", produces = "application/json")
	public ResponseFormat signin(@RequestBody UserSignInRequest user) {
		try {
			authenticate(user.getEmail(), user.getPassword());

			// UserDetails userDetails =
			// userDetailsService.loadUserByUsername(user.getEmail());
			UserDetailImp userDetails = userDetailsService.loadUserByEmail(user.getEmail());

			String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseFormat.builder().status(ResponseFormat.Status.success)
					.data(
							new UserSignInResponse(
									userDetails.getUid(),
									userDetails.getDisplayName(),
									"Bearer",
									token,
									this.refreshTokenService.generateRefresToken(user),
									userDetails.getUrlAvatar()
							)
					)
					.build();

		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error)
					.message(e.getMessage())
					.build();//new Resp("error", e.getMessage());
		}
	}

	@Override
	@PostMapping(value = "/refresh-token", produces = "application/json")
	public ResponseEntity<?> getTokenByRefreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		try {

			Optional<com.mywallet.api.entity.RefreshToken> s = this.refreshTokenService.findByToken(refreshToken.getRefreshToken().split("\\.")[0]);

			
			if (!s.isPresent()) {
				return ResponseEntity.ok(
						ResponseFormat.builder().status(ResponseFormat.Status.error)
								.message("refresh-token "+refreshToken.getRefreshToken()+" not found")
								.build()
				);
			} else {
				if (!this.refreshTokenService.isExpired(s.get())) {
					String token = this.jwtTokenUtil.generateTokenFromEmail(s.get().getUser().getEmail());
					
					JsonObject resp = new JsonObject();
					resp.addProperty("type", "Bearer");
					resp.addProperty("token", token);
					resp.addProperty("refreshToken", refreshToken.getRefreshToken());

					String respString = "{" + "\"status\": \"success\"," + "\"message\": null," + "\"data\": " + resp.toString() + "}";

					return ResponseEntity.ok(respString);
				}
				else {
					return ResponseEntity.ok(
							//new Resp("error", "refresh-token "+rt.getToken()+" has expired. Please sign in")
							ResponseFormat.builder().status(ResponseFormat.Status.error)
									.message("refresh-token \"+rt.getToken()+\" has expired. Please sign in")
									.build()
					);
				}
			}
			
		} catch (Exception e) {
			return ResponseEntity.ok(ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build());
		}
	}

	@Override
	@PostMapping(value = "/update", produces = "application/json")
	public ResponseFormat updateUserData(@RequestBody UserUpdateRequest userUpdate) {
		return this.userService.updateUserData(userUpdate);
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("USER DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID CREDENTIALS", e);
		}
	}



}
