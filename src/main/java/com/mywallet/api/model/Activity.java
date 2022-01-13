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

//	public Activity(String id, String walletId, String walletName, String titleActivity, String categoryActivity,
//			Double nominalActivity,
//			String descActivity, Date dateActivity, boolean income) {
//		this.id = id;
//		this.walletId = walletId;
//		this.walletName = walletName;
//		this.titleActivity = titleActivity;
//		this.categoryActivity = categoryActivity;
//		this.nominalActivity = nominalActivity;
//
//		this.descActivity = descActivity;
//		this.dateActivity = dateActivity;
//		this.income = income;
//
//	}

	@Override
	public String toString() {
		return  walletId + "#" + walletName
				+ "#" + title + "#" + desc + "#"
				+ nominal + "#" + date + "#" + income + "#"
				;
	}
	
	
}
