package com.mywallet.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class transfer {
	private String id;
	private String walletIdSource;
	private String walletIdDestination;
	private Double nominal;
	private Double fee;
}
