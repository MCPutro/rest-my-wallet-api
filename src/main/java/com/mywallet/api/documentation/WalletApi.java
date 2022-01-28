package com.mywallet.api.documentation;

import com.mywallet.api.entity.Wallet;
import com.mywallet.api.request.TransferRequest;
import com.mywallet.api.response.format.ResponseFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api
public interface WalletApi {

    @ApiOperation(value = "Create new wallet", response = ResponseFormat.class, nickname = "addWallet")
	public ResponseFormat addWallet(Wallet wallet, String UID) ;

    @ApiOperation(value = "Get list wallet by UID", response = Wallet.class, responseContainer = "List", nickname = "getAllWallet")
	public List<Wallet> getAllWallet(String UID);

    @ApiOperation(value = "Delete wallet by wallet Id", response = ResponseFormat.class, nickname = "removeWallet")
	public ResponseFormat removeWallet(String walletId);

    @ApiOperation(value = "Transfer between own wallet", response = ResponseFormat.class, nickname = "transfer")
	public ResponseFormat transfer(TransferRequest trf, String UID) ;

    @ApiOperation(value = "Cancel transfer between own wallet", response = ResponseFormat.class, nickname = "cancelTransfer")
    public ResponseFormat cancelTransfer(TransferRequest trf, String UID, String period);
}
