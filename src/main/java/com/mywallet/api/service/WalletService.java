package com.mywallet.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mywallet.api.entity.User;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.transfer;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.Data;
import com.mywallet.api.response.Resp;
import com.mywallet.api.response.transferResp;


@Service
public class WalletService {

	@Autowired private WalletRepository walletRepository;
	
	@Autowired private UserRepository userRepository;
	
	@Transactional
	public Resp addWallet(Wallet w, String uid) {
		Resp resp;
		try {
			User existing = this.userRepository.findByUid(uid);
			
			if (existing != null) {
				w.setUser(existing);
				this.walletRepository.save(w);
				
				resp = new Resp("success", null);
			}else {
				System.out.println("user gak ketemu");
				resp = new Resp("error", "use doesn't exist");
			}
			return resp;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new Resp("error", e.getMessage());
		}
	}
	
	@Transactional(readOnly = true)
	public List<Wallet> getAllWallet(String UID){
		User Existing = this.userRepository.findByUid(UID);
		return this.walletRepository.findByUser(Existing);
	}
	
	@Transactional
	public Resp removeWallet(String walletId) {
		try {
			/**check if wallet id is exists**/
			
			this.walletRepository.deleteById(walletId);
			
			return new Resp("success", null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new Resp("error", e.getMessage());
		}
	}
	
	@Transactional
	public Resp transferInternal(transfer trf) {
		try {
			Wallet w1 = this.walletRepository.findById(trf.getWalletIdSource()).orElse(null);
			
			Wallet w2 = this.walletRepository.findById(trf.getWalletIdDestination()).orElse(null);
			
			w1.setNominal(w1.getNominal() - (trf.getNominal() + trf.getFee()) );
			
			w2.setNominal(w2.getNominal() + trf.getNominal());
			
			this.walletRepository.save(w1);
			
			this.walletRepository.save(w2);
			
			return new Resp("success", null, new transferResp(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal()));
		} catch (Exception e) {
			return new Resp("error", null);
		}
	}
	
}
