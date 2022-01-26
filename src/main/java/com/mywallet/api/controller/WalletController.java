package com.mywallet.api.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.api.entity.Wallet;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.service.WalletService;
import com.mywallet.api.request.TransferRequest;
import com.mywallet.api.documentation.WalletApi;


@RestController
@RequestMapping("/api/wallet")
public class WalletController implements WalletApi {

	private final WalletService walletService;

	@Autowired
	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@PostMapping("/")
	public ResponseFormat addWallet(@RequestBody Wallet w, @RequestHeader String UID) {
		return this.walletService.addWallet(w, UID);
	}
	
	@GetMapping("/")
	public List<Wallet> getAllWallet(@RequestHeader String UID){
		return this.walletService.getAllWallet(UID);
	}
	
	@DeleteMapping("/")
	public ResponseFormat removeWallet(@RequestHeader String walletId) {
		return this.walletService.removeWallet(walletId);
	}
	
	@PostMapping("/transfer")
	public ResponseFormat transfer(@RequestBody TransferRequest trf, @RequestHeader String UID) {
		return this.walletService.transferInternal(trf, UID);
	}
	
	@PostMapping("/cancelTransfer")
		public ResponseFormat cancelTransfer(@RequestBody TransferRequest trf, @RequestHeader String UID, @RequestHeader String period) {
		return this.walletService.cancelTransferInternal(trf, UID, period);
	}
}
