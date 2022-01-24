package com.mywallet.api.response;

import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;

import com.mywallet.api.response.format.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ActivityUpdateResponse implements Data {

	private Wallet updatedWallet;
	
	private Activity updatedActivity;
}
