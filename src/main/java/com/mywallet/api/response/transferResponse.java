package com.mywallet.api.response;

import com.mywallet.api.model.Activity;

import com.mywallet.api.response.format.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class transferResponse implements Data {
	private String id;
	private newWalletData newWalletData;
	private Activity newActivity;

	public transferResponse(String id, String walletIdSource, Double nominalSource, String walletIdDestination, Double nominalDestination, Activity newActivity) {
		this.id = id;
		this.newWalletData = new newWalletData(walletIdSource, nominalSource, walletIdDestination, nominalDestination);
		this.newActivity = newActivity;
	}

	@Getter
	private class newWalletData{
		String walletIdSource;
		Double nominalSource;
		String walletIdDestination;
		Double nominalDestination;

		private newWalletData(String walletIdSource, Double nominalSource, String walletIdDestination, Double nominalDestination) {
			this.walletIdSource = walletIdSource;
			this.nominalSource = nominalSource;
			this.walletIdDestination = walletIdDestination;
			this.nominalDestination = nominalDestination;
		}
	}
	
}
