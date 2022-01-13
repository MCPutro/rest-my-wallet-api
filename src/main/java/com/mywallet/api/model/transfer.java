package com.mywallet.api.model;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class transfer {
	private String id;
	private String walletIdSource;
	private String walletIdDestination;
	private Double nominal;
	private Double fee;
}
