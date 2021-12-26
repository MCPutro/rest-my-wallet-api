package com.mywallet.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class transferResp implements Data{
	private String id;
	private String walletIdSource;
	private Double nominalSource;
	private String walletIdDestination;
	private Double nominalDestination;
}
