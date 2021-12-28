package com.mywallet.api.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailImp implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String password;

	private String username;

	private Set<GrantedAuthority> authorities;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	private String uid;
	
	private String displayName;
	
	private String urlAvatar;

	public UserDetailImp(String password, String username, String uid, String displayName, String urlAvatar) {
		this(password, username, null, true, true, true, true, uid, displayName, urlAvatar);
	}
}
