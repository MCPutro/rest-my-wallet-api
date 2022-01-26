package com.mywallet.api.service;

import com.mywallet.api.entity.User;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;
import com.mywallet.api.request.TransferRequest;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.response.transferResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService{

	private final WalletRepository walletRepository;
	
	private final UserRepository userRepository;
	
	private final FireBaseService fireBaseService;

	@Autowired
	public WalletServiceImpl(WalletRepository walletRepository, UserRepository userRepository, FireBaseService fireBaseService) {
		this.walletRepository = walletRepository;
		this.userRepository = userRepository;
		this.fireBaseService = fireBaseService;
	}

	@Transactional
	@Override
	public ResponseFormat addWallet(Wallet w, String uid) {

		try {
			ResponseFormat resp;
			User existing = this.userRepository.findByUid(uid);
			
			if (existing != null) {
				w.setUser(existing);
				this.walletRepository.save(w);
				
				resp = ResponseFormat.builder().status(ResponseFormat.Status.success).build();
			} else {
				System.out.println("user gak ketemu");
				resp = ResponseFormat.builder().status(ResponseFormat.Status.error).message("user doesn't exist").build();
			}
			return resp;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Wallet> getAllWallet(String UID){
		User Existing = this.userRepository.findByUid(UID);
		return this.walletRepository.findByUser(Existing);
	}
	
	@Transactional
	@Override
	public ResponseFormat removeWallet(String walletId) {
		try {

			this.walletRepository.deleteById(walletId);
			
			return ResponseFormat.builder().status(ResponseFormat.Status.success).build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}
	
	@Transactional
	@Override
	public ResponseFormat transferInternal(TransferRequest trf, String UID) {
		try {
			Wallet w1 = this.walletRepository.findById(trf.getWalletIdSource()).orElse(null);
			
			Wallet w2 = this.walletRepository.findById(trf.getWalletIdDestination()).orElse(null);

			assert w1 != null;
			w1.setNominal(w1.getNominal() - (trf.getNominal() + trf.getFee()) );

			assert w2 != null;
			w2.setNominal(w2.getNominal() + trf.getNominal());
			
			this.walletRepository.save(w1);
			
			this.walletRepository.save(w2);
			
			//create record for transfer activity
			Calendar c = Calendar.getInstance();
			
			c.setTime(new Date());
			
			String month = c.get(Calendar.MONTH)+1+"";
			
			Activity a = Activity.builder()
					.id(trf.getId())
					.walletId(w1.getId()+"##"+w2.getId())
					.walletName(w1.getName()+" -> "+w2.getName())
					.title("Transfer")
					.category("Transfer")
					.nominal(trf.getNominal())
					.desc("To: "+w2.getName()+", Fee: "+trf.getFee())
					.date(new Date())
					.income(false)
					.build();
			
			String err = this.fireBaseService.insertActivity(UID, 
					c.get(Calendar.YEAR) + "-" + ((month.length() == 1) ? ("0"+month) : month), 
					a);
			
			//return new Resp("success", null, new transferResp(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), a));
			return ResponseFormat.builder().status(ResponseFormat.Status.success)
					.data(new transferResponse(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), a)).build();
		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();//new Resp("error", null);
		}
	}
	
	@Transactional
	@Override
	public ResponseFormat cancelTransferInternal(TransferRequest trf, String UID, String period) {
		try {
			Wallet w1 = this.walletRepository.findById(trf.getWalletIdSource()).orElse(null);
			
			Wallet w2 = this.walletRepository.findById(trf.getWalletIdDestination()).orElse(null);

			assert w1 != null;
			w1.setNominal(w1.getNominal() + (trf.getNominal() + trf.getFee()) );

			assert w2 != null;
			w2.setNominal(w2.getNominal() - trf.getNominal());
			
			this.walletRepository.save(w1);
			
			this.walletRepository.save(w2);
			
			//remove record for transfer activity
			this.fireBaseService.getActivityCollectionReference(UID, period)
					.document(trf.getId())
					.delete()
					;
			//return new Resp("success", null, new transferResp(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), null));
			return ResponseFormat.builder().status(ResponseFormat.Status.success)
					.data(new transferResponse(trf.getId(), w1.getId(), w1.getNominal(), w2.getId(), w2.getNominal(), null)).build();
		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}
	
}
