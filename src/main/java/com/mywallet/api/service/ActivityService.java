package com.mywallet.api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.Query.Direction;
import com.google.gson.Gson;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;
import com.mywallet.api.model.ActivityList;
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.ActivityUpdateResponse;
import com.mywallet.api.response.Data;
import com.mywallet.api.response.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {

	@Autowired private WalletRepository walletRepository;

	@Autowired private FireBaseService fireBaseService;

	@Transactional
	public Resp createNewActivity(Activity newActivity, String UID) {
		try {
			
			if (newActivity.getId() == null) {
				newActivity.setId(UUID.randomUUID().toString());
			}
			Resp resp;
			Optional<Wallet> existingWallet = this.walletRepository.findById(newActivity.getWalletId());
			System.out.println(existingWallet.get());
			if (existingWallet.isPresent()) {

				// check if the balance is sufficient **/
				double walletNominal = existingWallet.get().getNominal();
				double balance;
				if (!newActivity.isIncome()) {
					balance = walletNominal - newActivity.getNominal();
				}else {
					balance = walletNominal + newActivity.getNominal();
				}

				if (balance >= 0) {
					// update nominal wallet **/
					existingWallet.get().setNominal(balance);

					//update balance to db
					walletRepository.save(existingWallet.get());

					Calendar c = Calendar.getInstance();

					c.setTime(newActivity.getDate());

					String month = c.get(Calendar.MONTH)+1+"";

					String err = this.fireBaseService.insertActivity(UID,
							c.get(Calendar.YEAR) + "-" + ((month.length() == 1) ? ("0"+month) : month),
							newActivity);

					if (err == null) {
						resp = Resp.builder()
								.status(Resp.Status.success)
								.data(new Data() {
									public final String activityId = newActivity.getId();
									public final String walletId = existingWallet.get().getId();
									public final Double remainingBalance = balance;
								})
								.build();

					} else {
						resp = Resp.builder().status(Resp.Status.error).message(err).build();
					}

				} else {
					resp = Resp.builder().status(Resp.Status.error).message("not enough balance").build();
				}
				System.out.println("end : " + LocalDateTime.now());
			} else {
				resp = Resp.builder().status(Resp.Status.error).message("wallet id " + newActivity.getWalletId() + " doesn't exists").build();
			}

			return resp;
		} catch (Throwable e) {
			System.out.println(
					"ActivityService|createNewActivity|error|" + LocalDateTime.now() + "|" + e.getMessage()+"|"+new Gson().toJson(newActivity));
			return Resp.builder().status(Resp.Status.error).message(e.getMessage()).build();
		}
	}

	

	@Transactional
	public Resp getActivities(String UID, String period) {
		try {
			Resp resp;
			CollectionReference cities = this.fireBaseService.getActivityCollectionReference(UID, period);
			Query query = cities
					.orderBy("date", Direction.DESCENDING)
					//.limit(30)
					;

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			ArrayList<Activity> tmp = new ArrayList<>();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {

				tmp.add(Activity.builder()
						.id(document.getString("id"))
						.walletId(document.getString("walletId"))
						.walletName(document.getString("walletName"))
						.title(document.getString("title"))
						.category(document.getString("category"))
						.nominal(document.getDouble("nominal"))
						.date(document.getDate("date"))
						.income(document.getBoolean("income"))
						.build()
					);
			}
			

			resp = Resp.builder()
					.status(Resp.Status.success)
					.data(ActivityList.builder().period(period).activities(tmp).build())
					.build();

			return resp;
		} catch (Exception e) {
			return Resp.builder()
					.status(Resp.Status.error)
					.message(e.getMessage())
					.build();
		}

	}

	@Transactional
	public Resp removeActivity(String uid, String activityId, String period) {
		try {
			DocumentReference writeResult = this.fireBaseService.getActivityCollectionReference(uid, period)
					.document(activityId)
					//.get()
					//.delete()
					;
			
			if (writeResult.get().get().getString("walletId") != null) {
				/**update nominal wallet**/
				Wallet existingWallet = this.walletRepository.findById(writeResult.get().get().getString("walletId")).orElse(null);
				if (existingWallet != null) {
					if (writeResult.get().get().getBoolean("income")) {
						existingWallet.setNominal(existingWallet.getNominal() - writeResult.get().get().getDouble("nominal"));
					}else {
						existingWallet.setNominal(existingWallet.getNominal() + writeResult.get().get().getDouble("nominal"));
					}
					
					this.walletRepository.save(existingWallet);		
				}
				
				//System.out.println("nominal : " + writeResult.get().get().getDouble("nominalActivity"));
				writeResult.delete();

				return Resp.builder()
						.status(Resp.Status.success)
						.data(new Data() {
							public final String walletid = existingWallet.getId();
							public final Double remainingBalance = existingWallet.getNominal();
						})
						.build();
			}else {
				return Resp.builder().status(Resp.Status.error).message("data not found.").build();
			}
			
		} catch (Throwable e) {
			return Resp.builder().status(Resp.Status.error).message(e.getMessage()).build();
		}
	}
	
	@Transactional
	public Resp updateActivity(String UID, String period, Activity activity) {
		
		try {
			//get prev wallet 
			Wallet prevWallet = this.walletRepository.findById(activity.getWalletId()).orElse(null);
			if (prevWallet != null) {
				//get prev activity 
				ApiFuture<DocumentSnapshot> doc = this.fireBaseService.getActivityCollectionReference(UID, period).document(activity.getId()).get();
				
				if (doc.get().getString("id") == null) {
					return Resp.builder().status(Resp.Status.error).message("Activity not found").build();
				}
				
				Activity prevActivity = Activity.builder()
						.id(doc.get().getString("id"))
						.walletId(doc.get().getString("walletId"))
						.walletName(doc.get().getString("walletName"))
						.title(doc.get().getString("title"))
						.category(doc.get().getString("category"))
						.nominal(doc.get().getDouble("nominal"))
						.date(doc.get().getDate("date"))
						.income(doc.get().getBoolean("income"))
						.build();

				//update nominal if its change
				if (activity.getNominal() != prevActivity.getNominal()) {
					if (prevActivity.isIncome()) {
						prevWallet.setNominal(prevWallet.getNominal() - prevActivity.getNominal());
						
						prevWallet.setNominal(prevWallet.getNominal() + activity.getNominal());
					}else {
						prevWallet.setNominal(prevWallet.getNominal() + prevActivity.getNominal());
						
						prevWallet.setNominal(prevWallet.getNominal() - activity.getNominal());
					}
				}
				
			
				String err = this.fireBaseService.insertActivity(UID, period, activity);
				
				if (err == null) {
					if (activity.getNominal() != prevActivity.getNominal()) {
						this.walletRepository.save(prevWallet);
					}
					System.out.println("ok "+activity.getDate());
					return Resp.builder()
							.status(Resp.Status.success)
							.data(new ActivityUpdateResponse(prevWallet, activity))
							.build();
				}else {
					return Resp.builder().status(Resp.Status.error).message(err).build();
				}
			}
			
			return Resp.builder().status(Resp.Status.error).message("Internal Server Error").build();
			
			
		} catch (Exception e) {
			return Resp.builder().status(Resp.Status.error).message(e.getMessage()).build();
		}
		
		
	}
}
