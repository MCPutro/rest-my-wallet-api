package com.mywallet.api.model;

import java.util.Date;
import java.util.UUID;

import lombok.*;

@Setter
@Getter
@Builder
public class Activity {

	private String id;
	private String walletId;
	private String walletName;
	private String title;
	private String category;
	private String desc;
	private Double nominal;
	private Date date;
	private boolean income;

	@Override
	public String toString() {
		return  walletId + "#" + walletName
				+ "#" + title + "#" + desc + "#"
				+ nominal + "#" + date + "#" + income + "#"
				;
	}
	
	
}
