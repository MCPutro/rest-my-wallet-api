package com.mywallet.api.response.model;

import com.mywallet.api.response.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Data {

	private String uid;
	
	private String username;
	
	private String email;
	
	private String urlAvatar;
	
	
}
