package com.mywallet.api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.Query.Direction;
import com.google.gson.Gson;
import com.mywallet.api.entity.Wallet;
import com.mywallet.api.model.Activity;
import com.mywallet.api.model.ActivityList;
import com.mywallet.api.repository.WalletRepository;
import com.mywallet.api.response.ActivityCreateResponse;
import com.mywallet.api.response.ActivityUpdateResponse;
import com.mywallet.api.response.format.Data;
import com.mywallet.api.response.format.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService{

	private final WalletRepository walletRepository;

	private final FireBaseService fireBaseService;

	@Autowired
	public ActivityServiceImpl(WalletRepository walletRepository, FireBaseService fireBaseService) {
		this.walletRepository = walletRepository;
		this.fireBaseService = fireBaseService;
	}

	@Transactional
	@Override
	public ResponseFormat createNewActivity(Activity newActivity, String UID) {
		try {
			if (newActivity.getId() == null) newActivity.setId(UUID.randomUUID().toString());

			//ResponseFormat resp;
			Calendar c = Calendar.getInstance();
			c.setTime(newActivity.getDate());
			String month = c.get(Calendar.MONTH)+1+"";

			if (newActivity.getType() != Activity.ActivityType.TRANSFER){

				Optional<Wallet> existingWallet = this.walletRepository.findById(newActivity.getWalletId());
				//System.out.println(existingWallet.get());
				if (existingWallet.isPresent()) {

					// calculate balance **/
					double walletNominal = existingWallet.get().getNominal();
					double balance = newActivity.getNominal();
					if (newActivity.getType() == Activity.ActivityType.EXPENSE){
						balance = walletNominal - newActivity.getNominal();
					} else if (newActivity.getType() == Activity.ActivityType.INCOME){
						balance = walletNominal + newActivity.getNominal();
					}

					// check if the balance is sufficient **/
					if (balance >= 0) {
						// update nominal wallet **/
						existingWallet.get().setNominal(balance);

						//update balance to db
						walletRepository.save(existingWallet.get());

						//insert new activity to firestore
						String err = push2firestore(UID, month, c, newActivity);

						if (err == null) {
							return ResponseFormat.builder()
									.status(ResponseFormat.Status.success)
									.data(new ActivityCreateResponse(newActivity.getId(), existingWallet.get().getId(), balance))
									.build();
						} else {
							return ResponseFormat.builder().status(ResponseFormat.Status.error).message(err).build();
						}

					} else {
						return ResponseFormat.builder().status(ResponseFormat.Status.error).message("not enough balance").build();
					}
					//System.out.println("end : " + LocalDateTime.now());
				} else {
					return ResponseFormat.builder().status(ResponseFormat.Status.error).message("wallet id " + newActivity.getWalletId() + " doesn't exists").build();
				}
			} else {
				// transfer type
				//insert new activity to firestore
				String err = push2firestore(UID, month, c, newActivity);
				if (err == null) {
					return ResponseFormat.builder()
							.status(ResponseFormat.Status.success)
							.build();
				}else{
					return ResponseFormat.builder().status(ResponseFormat.Status.error).message(err).build();
				}
			}
		} catch (Throwable e) {
			System.out.println(
					"ActivityService|createNewActivity|error|" + LocalDateTime.now() + "|" + e.getMessage()+"|"+new Gson().toJson(newActivity));
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}

	private String push2firestore(String UID, String month, Calendar c, Activity newActivity){
		return this.fireBaseService.insertActivity(UID,
				c.get(Calendar.YEAR) + "-" + ((month.length() == 1) ? ("0"+month) : month),
				newActivity);

	}

	@Transactional(readOnly = true)
	@Override
	public ResponseFormat getActivities(String UID, String period) {
		try {
			ResponseFormat resp;
			CollectionReference cities = this.fireBaseService.getActivityCollectionReference(UID, period);
			Query query = cities
					.orderBy("date", Direction.DESCENDING)
					//.limit(30)
					;

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			ArrayList<Activity> tmp = new ArrayList<>();

			for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {

				tmp.add(Activity.builder()
						.id(doc.getString("id"))
						.walletId(doc.getString("walletId"))
						.walletName(doc.getString("walletName"))
						.title(doc.getString("title"))
						.category(doc.getString("category"))
						.nominal(doc.getDouble("nominal"))
						.date(doc.getDate("date"))
						.type(Activity.ActivityType.valueOf(doc.getString("type")))//.income(doc.getBoolean("income"))
						.desc(doc.getString("desc"))
						.build()
					);
			}


			resp = ResponseFormat.builder()
					.status(ResponseFormat.Status.success)
					.data(ActivityList.builder().period(period).activities(tmp).build())
					.build();

			return resp;
		} catch (Exception e) {
			return ResponseFormat.builder()
					.status(ResponseFormat.Status.error)
					.message(e.getMessage())
					.build();
		}

	}

	@Transactional
	@Override
	public ResponseFormat removeActivity(String uid, String activityId, String period) {
		try {
			DocumentReference writeResult = this.fireBaseService.getActivityCollectionReference(uid, period)
					.document(activityId)
					//.get()
					//.delete()
					;

			if (writeResult.get().get().getString("walletId") != null) {
				ResponseFormat resp = ResponseFormat.builder().status(ResponseFormat.Status.success).build();
				boolean isTransfer = Activity.ActivityType.valueOf(writeResult.get().get().getString("type")) == Activity.ActivityType.TRANSFER;

				if (!isTransfer) {
					// update nominal wallet
					Wallet existingWallet = this.walletRepository.findById(writeResult.get().get().getString("walletId")).orElse(null);
					if (existingWallet != null) {
						if(Activity.ActivityType.valueOf(writeResult.get().get().getString("type")) == Activity.ActivityType.INCOME){//if (writeResult.get().get().getBoolean("income")) {
							existingWallet.setNominal(existingWallet.getNominal() - writeResult.get().get().getDouble("nominal"));
						}else {
							existingWallet.setNominal(existingWallet.getNominal() + writeResult.get().get().getDouble("nominal"));
						}
						this.walletRepository.save(existingWallet);
						resp = ResponseFormat.builder()
								.status(ResponseFormat.Status.success)
								.data(new Data() {
									public final String walletid = existingWallet.getId();
									public final Double remainingBalance = existingWallet.getNominal();
								})
								.build();
					}
				}

				writeResult.delete();
				return resp;
			} else {
				return ResponseFormat.builder().status(ResponseFormat.Status.error).message("data not found.").build();
			}

		} catch (Throwable e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
	}

	@Transactional
	@Override
	public ResponseFormat updateActivity(String UID, String period, Activity activity) {

		try {
			//get prev wallet
			Wallet prevWallet = this.walletRepository.findById(activity.getWalletId()).orElse(null);
			if (prevWallet != null) {
				//get prev activity
				ApiFuture<DocumentSnapshot> doc = this.fireBaseService.getActivityCollectionReference(UID, period).document(activity.getId()).get();

				if (doc.get().getString("id") == null) {
					return ResponseFormat.builder().status(ResponseFormat.Status.error).message("Activity not found").build();
				}

				Activity prevActivity = Activity.builder()
						.id(doc.get().getString("id"))
						.walletId(doc.get().getString("walletId"))
						.walletName(doc.get().getString("walletName"))
						.title(doc.get().getString("title"))
						.category(doc.get().getString("category"))
						.nominal(doc.get().getDouble("nominal"))
						.date(doc.get().getDate("date"))
						.type(Activity.ActivityType.valueOf(doc.get().getString("type")))//.income(doc.get().getBoolean("income"))
						.build();

				//update nominal if its change
				if (activity.getNominal() != prevActivity.getNominal()) {
					if (prevActivity.getType() == Activity.ActivityType.INCOME){//if (prevActivity.isIncome()) {
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
					return ResponseFormat.builder()
							.status(ResponseFormat.Status.success)
							.data(new ActivityUpdateResponse(prevWallet, activity))
							.build();
				}else {
					return ResponseFormat.builder().status(ResponseFormat.Status.error).message(err).build();
				}
			}

			return ResponseFormat.builder().status(ResponseFormat.Status.error).message("Internal Server Error").build();


		} catch (Exception e) {
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}


	}
}
