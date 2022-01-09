package com.mywallet.api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.firestore.DocumentReference;
import com.mywallet.api.entity.User;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;
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
	
	@Autowired private FireBaseService fireBaseService;
	
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
	public Resp transferInternal(transfer trf, String UID) {
		try {
			Wallet w1 = this.walletRepository.findById(trf.getWalletIdSource()).orElse(null);
			
			Wallet w2 = this.walletRepository.findById(trf.getWalletIdDestination()).orElse(null);
			
			w1.setNominal(w1.getNominal() - (trf.getNominal() + trf.getFee()) );
			
			w2.setNominal(w2.getNominal() + trf.getNominal());
			
			this.walletRepository.save(w1);
			
			this.walletRepository.save(w2);
			
			//create record for transfer activity
			Calendar c = Calendar.getInstance();
			
			c.setTime(new Date());
			
			String month = c.get(Calendar.MONTH)+1+"";
			
			Activity a = new Activity(
					trf.getId(),
					w1.getId()+"##"+w2.getId(),
					w1.getName()+" -> "+w2.getName(),
					"Transfer",
					"Transfer",
					trf.getNominal(),
					"To: "+w2.getName()+", Fee: "+trf.getFee(),
					new Date(),
					false
					);
			
			String err = this.fireBaseService.insertActivity(UID, 
					c.get(Calendar.YEAR) + "-" + ((month.length() == 1) ? ("0"+month) : month), 
					a);
			
			return new Resp("success", null, new transferResp(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), a));
		} catch (Exception e) {
			return new Resp("error", null);
		}
	}
	
	@Transactional
	public Resp cancelTransferInternal(transfer trf, String UID, String period) {
		try {
			Wallet w1 = this.walletRepository.findById(trf.getWalletIdSource()).orElse(null);
			
			Wallet w2 = this.walletRepository.findById(trf.getWalletIdDestination()).orElse(null);
			
			w1.setNominal(w1.getNominal() + (trf.getNominal() + trf.getFee()) );
			
			w2.setNominal(w2.getNominal() - trf.getNominal());
			
			this.walletRepository.save(w1);
			
			this.walletRepository.save(w2);
			
			//remove record for transfer activity
			this.fireBaseService.getActivityCollectionReference(UID, period)//db.collection(uid).document(DocumentLabel.activity).collection(period)
					.document(trf.getId())
					.delete()
					;
			
			
			return new Resp("success", null, new transferResp(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), null));
		} catch (Exception e) {
			return new Resp("error", null);
		}
	}
	
}
