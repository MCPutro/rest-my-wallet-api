package com.mywallet.api.response.format;

import java.io.Serializable;

import lombok.*;

@Getter
@Builder
public class ResponseFormat implements Serializable{

	private static final long serialVersionUID = -6626462738725154001L;

	public enum Status {
		success,
		error
	}

	private Status status;
	
	private String message;
	
	private Data data;

	
}
