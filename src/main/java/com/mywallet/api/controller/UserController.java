package com.mywallet.api.controller;

import java.util.Optional;

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
import com.mywallet.api.entity.RefreshToken;
import com.mywallet.api.entity.User;
import com.mywallet.api.model.UserDetailImp;
import com.mywallet.api.response.Resp;
import com.mywallet.api.response.model.UserSignInResponse;
import com.mywallet.api.service.RefreshTokenService;
import com.mywallet.api.service.UserService;
import com.mywallet.api.service.jwt.JwtUserDetailsService;

import com.google.gson.JsonObject;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

	private UserService userService;

	private JwtTokenUtil jwtTokenUtil;

	private AuthenticationManager authenticationManager;

	private JwtUserDetailsService userDetailsService;

	private RefreshTokenService refreshTokenService;

	@Autowired
	public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.refreshTokenService = refreshTokenService;
	}

	@GetMapping("/")
	public String onAir() {
		return "OK";
	}

	@PostMapping(value = "/createAndLogin" , produces = "application/json")
	public ResponseEntity<?> createAndLogin(@RequestBody User user) {
		String temp = user.getPassword();
		JsonObject resp = new JsonObject();
		
		Resp create = this.userService.insertUser(user);
		
		if (create.getStatus() == Resp.Status.success){
			
			User firstLogin = new User();
			firstLogin.setEmail(user.getEmail());
			firstLogin.setPassword(temp);
			
			Resp p = this.signin(firstLogin);
			
			return ResponseEntity.ok(p);
		}else {
			resp.addProperty("status", create.getStatus().toString());
			resp.addProperty("message", create.getMessage());
			return ResponseEntity.ok(resp.toString());
		}
			
		//return null;
	}
	
	@PostMapping("/signup")
	public Resp signup(@RequestBody User newUser) {
		return this.userService.insertUser(newUser);
	}

	@PostMapping(value = "/signin", produces = "application/json")
	public Resp signin(@RequestBody User user) {
		try {
			authenticate(user.getEmail(), user.getPassword());

			// UserDetails userDetails =
			// userDetailsService.loadUserByUsername(user.getEmail());
			UserDetailImp userDetails = userDetailsService.loadUserByEmail(user.getEmail());

			String token = jwtTokenUtil.generateToken(userDetails);

			return Resp.builder().status(Resp.Status.success)
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
			return Resp.builder().status(Resp.Status.error)
					.message(e.getMessage())
					.build();//new Resp("error", e.getMessage());
		}
	}

	@PostMapping(value = "/refresh-token", produces = "application/json")
	public ResponseEntity<?> getTokenByRefreshToken(@RequestBody RefreshToken rt) {
		try {
			Optional<RefreshToken> s = this.refreshTokenService.findByToken(rt.getToken().split("\\.")[0]);
			
			if (!s.isPresent()) {
				return ResponseEntity.ok(
						Resp.builder().status(Resp.Status.error)
								.message("refresh-token "+rt.getToken()+" not found")
								.build()
				);
			} else {
				if (!this.refreshTokenService.isExpired(s.get())) {
					String token = this.jwtTokenUtil.generateTokenFromEmail(s.get().getUser().getEmail());
					
					JsonObject resp = new JsonObject();
					resp.addProperty("type", "Bearer");
					resp.addProperty("token", token);
					resp.addProperty("refreshToken", rt.getToken());

					String respString = "{" + "\"status\": \"success\"," + "\"message\": null," + "\"data\": " + resp.toString() + "}";

					return ResponseEntity.ok(respString);
				}
				else {
					return ResponseEntity.ok(
							//new Resp("error", "refresh-token "+rt.getToken()+" has expired. Please sign in")
							Resp.builder().status(Resp.Status.error)
									.message("refresh-token \"+rt.getToken()+\" has expired. Please sign in")
									.build()
					);
				}
			}
			
		} catch (Exception e) {
			return ResponseEntity.ok(Resp.builder().status(Resp.Status.error).message(e.getMessage()).build());
		}
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
	
	@PostMapping(value = "/update", produces = "application/json")
	public Resp updateUserData(@RequestBody User user) {
		
		return this.userService.updateUserData(user);
	}

}
