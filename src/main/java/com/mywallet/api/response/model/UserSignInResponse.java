package com.mywallet.api.response.model;

import com.mywallet.api.response.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserSignInResponse implements Data {

	private String uid;
	
	private String username;
	
	private String type;
	
	private String token;
	
	private String refreshToken;
	
	private String urlAvatar;
	
}
