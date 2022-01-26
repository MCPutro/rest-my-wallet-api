package com.mywallet.api.service;

import com.mywallet.api.entity.Wallet;
import com.mywallet.api.request.TransferRequest;
import com.mywallet.api.response.format.ResponseFormat;

import java.util.List;

public interface WalletService {
    ResponseFormat addWallet(Wallet w, String uid);
    List<Wallet> getAllWallet(String UID);
    ResponseFormat removeWallet(String walletId);
    ResponseFormat transferInternal(TransferRequest trf, String UID);
    ResponseFormat cancelTransferInternal(TransferRequest trf, String UID, String period);
}
