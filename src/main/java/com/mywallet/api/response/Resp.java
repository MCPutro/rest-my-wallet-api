package com.mywallet.api.response;

import java.io.Serializable;

import lombok.*;

@Getter
@Builder
public class Resp implements Serializable{

	private static final long serialVersionUID = -6626462738725154001L;

	public enum Status {
		success,
		error
	}

	private Status status;
	
	private String message;
	
	private Data data;

//	public Resp(String status, String message) {
//		super();
//		this.status = status;
//		this.message = message;
//	}
	
	
}
