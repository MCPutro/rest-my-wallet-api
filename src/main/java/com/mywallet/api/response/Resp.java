package com.mywallet.api.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Resp implements Serializable{

	private static final long serialVersionUID = -6626462738725154001L;

	private String status;
	
	private String message;
	
	private Data data;

	public Resp(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	
}
