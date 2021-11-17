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
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.Resp;
import com.mywallet.api.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

	@Autowired private WalletService walletService;
	
	@PostMapping("/update")
	public Resp addWallet(@RequestBody Wallet w, @RequestHeader String UID) {
		return this.walletService.addWallet(w, UID);
	}
	
	@GetMapping("/getByUID")
	public List<Wallet> getAllWallet(@RequestHeader String UID){
		return this.walletService.getAllWallet(UID);
	}
	
	@DeleteMapping("/")
	public Resp removeWallet(@RequestHeader String walletId) {
		return this.walletService.removeWallet(walletId);
	}
}
