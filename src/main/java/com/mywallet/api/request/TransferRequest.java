package com.mywallet.api.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class TransferRequest {
	private String id;
	private String walletIdSource;
	private String walletIdDestination;
	private Double nominal;
	private Double fee;
	private Date date;
}
