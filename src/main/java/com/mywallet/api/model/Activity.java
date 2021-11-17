package com.mywallet.api.model;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

	private String id;
	private String walletId;
	private String walletName;
	private String titleActivity;
	private String descActivity;
	private Double nominalActivity;
	private Date dateActivity;
	private boolean income;//, expands;

	public Activity(String walletId, String walletName, String titleActivity, Double nominalActivity, // int icon_history,
			String descActivity, Date dateActivity, boolean income) {
		this.id = "+" + UUID.randomUUID().toString();
		this.walletId = walletId;
		this.walletName = walletName;
		this.titleActivity = titleActivity;
		this.nominalActivity = nominalActivity;
		// this.icon_history = icon_history;
		this.descActivity = descActivity;
		this.dateActivity = dateActivity;
		this.income = income;
		//this.expands = false;
	}

	public Activity(String id, String walletId, String walletName, String titleActivity,
			Double nominalActivity, // int icon_history,
			String descActivity, Date dateActivity, boolean income) {
		this.id = id;
		this.walletId = walletId;
		this.walletName = walletName;
		this.titleActivity = titleActivity;
		this.nominalActivity = nominalActivity;
		// this.icon_history = icon_history;
		this.descActivity = descActivity;
		this.dateActivity = dateActivity;
		this.income = income;
		//this.expands = false;
	}

	@Override
	public String toString() {
		return  walletId + "#" + walletName
				+ "#" + titleActivity + "#" + descActivity + "#"
				+ nominalActivity + "#" + dateActivity + "#" + income + "#"
				//+ expands
				;
	}
	
	
}
