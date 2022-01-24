package com.mywallet.api.documentation;

import com.mywallet.api.entity.Wallet;
import com.mywallet.api.request.TransferRequest;
import com.mywallet.api.response.format.ResponseFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api
public interface WalletApi {

    @ApiOperation(value = "test1", response = ResponseFormat.class)
	public ResponseFormat addWallet(Wallet w, String UID) ;

    @ApiOperation(value = "test1", response = Iterable.class)
	public List<Wallet> getAllWallet(String UID);

    @ApiOperation(value = "test1", response = ResponseFormat.class)
	public ResponseFormat removeWallet(String walletId);

    @ApiOperation(value = "test1", response = ResponseFormat.class)
	public ResponseFormat transfer(TransferRequest trf, String UID) ;

    @ApiOperation(value = "test1", response = ResponseFormat.class)
    public ResponseFormat cancelTransfer(TransferRequest trf, String UID, String period);
}
