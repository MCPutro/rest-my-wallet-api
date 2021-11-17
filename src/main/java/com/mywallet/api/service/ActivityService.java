package com.mywallet.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;
import com.mywallet.api.model.ActivityList;
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.ActivityUpdateResponse;
import com.mywallet.api.response.Resp;

@Service
public class ActivityService {

	@Autowired private WalletRepository walletRepository;

	@Autowired private FireBaseService fireBaseService;	
	/**@Autowired
	private Firestore db;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ObjectMapper mapper;**/

	// @Autowired
	//private LocalDateTime currentDateTime;

	@Transactional
	public Resp createNewActivity(Activity newActivity, String UID) {
		try {
			//System.out.println("start : " + LocalDateTime.now());
			newActivity.setId(UUID.randomUUID().toString());
			Resp resp;
			Optional<Wallet> existingWallet = this.walletRepository.findById(newActivity.getWalletId());
			if (existingWallet.isPresent()) {

				/** check if the balance is sufficient **/
				double walletNominal = existingWallet.get().getNominal();
				double balance;
				if (!newActivity.isIncome()) {
					balance = walletNominal - newActivity.getNominalActivity();
				}else {
					balance = walletNominal + newActivity.getNominalActivity();
				}
				
				if (balance >= 0) {
					/** update nominal wallet **/
					existingWallet.get().setNominal(balance);
					/**ArrayList<String> tmp = existingWallet.get().getMutasi();
					if (tmp == null || tmp.isEmpty()) {
						tmp = new ArrayList<>();
					}

					if (tmp.size() == Integer.parseInt(env.getProperty("limitActivity"))) {
						tmp.remove(tmp.size() - 1);
					}

					tmp.add(0, newActivity.toString());
					existingWallet.get().setMutasi(tmp);**/

					/** update balance to db **/
					walletRepository.save(existingWallet.get());

					String err = this.fireBaseService.insertActivity(UID,
							LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue(),
							newActivity);

					/** return resp **/
					if (err == null) {
						resp = new Resp("success", null);
					} else {
						resp = new Resp("error", err);
					}

				} else {
					resp = new Resp("error", "not enough balance");
				}
				System.out.println("end : " + LocalDateTime.now());
			} else {
				resp = new Resp("error", "wallet id " + newActivity.getWalletId() + " doesn't exists");
			}

			return resp;
		} catch (Throwable e) {
			System.out.println(
					"ActivityService|createNewActivity|error|" + LocalDateTime.now() + "|" + e.getMessage()+"|"+new Gson().toJson(newActivity));
			return new Resp("error", e.getMessage());
		}
	}

	

	@Transactional
	public Resp getActivities(String UID, String period) {
		try {
			Resp resp = null;
			CollectionReference cities = this.fireBaseService.getActivityCollectionReference(UID, period);//db.collection(UID).document(DocumentLabel.activity).collection(period);
			Query query = cities
					.orderBy("dateActivity", Direction.DESCENDING)
					//.limit(30)
					;

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			ArrayList<Activity> tmp = new ArrayList<>();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				
//				System.out.println("??"+document.getDate("dateActivity"));
				tmp.add(new Activity(document.getString("id"), 
						document.getString("walletId"),
						document.getString("walletName"), 
						document.getString("titleActivity"),
						document.getString("descActivity"), 
						document.getDouble("nominalActivity"),
						document.getDate("dateActivity"), 
						document.getBoolean("income")//,
						//document.getBoolean("expands")
						)
					);
			}
			

			resp = new Resp("success", null, new ActivityList(period, tmp));

			return resp;
		} catch (Exception e) {
			return new Resp("error", e.getMessage());
		}

	}

	@Transactional
	public Resp removeActivity(String uid, String activityId, String period) {
		try {
			DocumentReference writeResult = this.fireBaseService.getActivityCollectionReference(uid, period)//db.collection(uid).document(DocumentLabel.activity).collection(period)
					.document(activityId)
					//.get()
					//.delete()
					;
			
			if (writeResult.get().get().getString("walletId") != null) {
				/**update nominal wallet**/
				Wallet existingWallet = this.walletRepository.findById(writeResult.get().get().getString("walletId")).orElse(null);
				if (existingWallet != null) {
					existingWallet.setNominal(existingWallet.getNominal() + writeResult.get().get().getDouble("nominalActivity"));
					this.walletRepository.save(existingWallet);		
				}
				
				//System.out.println("nominal : " + writeResult.get().get().getDouble("nominalActivity"));
				writeResult.delete();
				
				return new Resp("success", null);
			}else {
				return new Resp("error", "data not found.");
			}
			
		} catch (Throwable e) {
			return new Resp("error", e.getMessage());
		}
	}
	
	@Transactional
	public Resp updateActivity(String UID, String period, Activity activity) {
		
		try {
			//get prev wallet 
			Wallet prevWallet = this.walletRepository.findById(activity.getWalletId()).orElse(null);
			if (prevWallet != null) {
				//get prev activity 
				ApiFuture<DocumentSnapshot> xxx = this.fireBaseService.getActivityCollectionReference(UID, period).document(activity.getId()).get();
				
				if (xxx.get().getString("id") == null) {
					return new Resp("error", "Activity not found");
				}
				
				Activity prevActivity = new Activity(
						xxx.get().getString("id"), 
						xxx.get().getString("walletId"), 
						xxx.get().getString("walletName"),
						xxx.get().getString("titleActivity"),
						xxx.get().getString("descActivity"),
						xxx.get().getDouble("nominalActivity"),
						xxx.get().getDate("dateActivity"), 
						xxx.get().getBoolean("income")//,
						//xxx.get().getBoolean("expands")
						);
				
				//update nominal if its change
				if (activity.getNominalActivity() != prevActivity.getNominalActivity()) {
					if (prevActivity.isIncome()) {
						prevWallet.setNominal(prevWallet.getNominal() - prevActivity.getNominalActivity());
						
						prevWallet.setNominal(prevWallet.getNominal() + activity.getNominalActivity());
					}else {
						prevWallet.setNominal(prevWallet.getNominal() + prevActivity.getNominalActivity());
						
						prevWallet.setNominal(prevWallet.getNominal() - activity.getNominalActivity());
					}
				}
				
			
				String err = this.fireBaseService.insertActivity(UID, period, activity);
				
				if (err == null) {
					if (activity.getNominalActivity() != prevActivity.getNominalActivity()) {
						this.walletRepository.save(prevWallet);
					}
					System.out.println("ok "+activity.getDateActivity());
					return new Resp("success", null, new ActivityUpdateResponse(prevWallet, activity));
					
				}else {
					return new Resp("error", err);
				}
			}
			
			return new Resp("error", "Internal Server Error");
			
			
		} catch (Exception e) {
			return new Resp("error", e.getMessage());
		}
		
		
	}
}
