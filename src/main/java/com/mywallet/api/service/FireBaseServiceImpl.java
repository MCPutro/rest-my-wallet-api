package com.mywallet.api.service;

import java.util.Random;

import com.mywallet.api.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.*;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.mywallet.api.config.DocumentLabel;
import com.mywallet.api.entity.User;
import com.mywallet.api.model.Activity;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.response.UserResponse;

@Service
public class FireBaseServiceImpl implements FireBaseService{

	private final FirebaseAuth firebaseAuth;
	
	private final Firestore db;
	
	private Random r;
	
	private final String[] url_image = {
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(1).png?alt=media&token=153551e6-5ce5-4632-a4bf-3d2a0d964658",
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(2).png?alt=media&token=9df29c80-14ca-4b91-b105-5270a5ea1b30",
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(3).png?alt=media&token=0f20be54-5af2-4cd0-947d-5f31de405eed",
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(4).png?alt=media&token=1a646b90-cf9d-4eb6-af2e-df926346910a",
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(5).png?alt=media&token=34880bf5-3057-4c54-8c9a-94713aea002a",
			"https://firebasestorage.googleapis.com/v0/b/my-wallet-api-93eaa.appspot.com/o/default%2Favatar(6).png?alt=media&token=ca158247-2925-4628-bb56-4a2a6372f912"
	};

	@Autowired
	public FireBaseServiceImpl(FirebaseAuth firebaseAuth, Firestore db, Random r) {
		this.firebaseAuth = firebaseAuth;
		this.db = db;
		this.r = r;
	}

	@Transactional
	public ResponseFormat createUser(User baru) {
		try {
			
			System.out.println("Start created new user: ");
			CreateRequest request = new CreateRequest()
					.setEmail(baru.getEmail())
					.setEmailVerified(false)
					.setPassword(baru.getPassword())
					// .setPhoneNumber(null)
					.setDisplayName(baru.getUsername())
					.setPhotoUrl(url_image[r.nextInt(url_image.length)])
					.setDisabled(false);
			//System.out.println("Start created new user: ");
			
			UserRecord userRecord = this.firebaseAuth.createUser(request);
			//System.out.println("Successfully created new user: " + userRecord.getUid());
			
			return  ResponseFormat.builder()
					.status(ResponseFormat.Status.success)
					.data(new UserResponse(userRecord.getUid(), userRecord.getDisplayName(), userRecord.getEmail(), userRecord.getPhotoUrl()))
					.build();


		} catch (Exception e) {
			System.out.println("createUser|error|" + e.getMessage());
			return ResponseFormat.builder().status(ResponseFormat.Status.error).message(e.getMessage()).build();
		}
		
	}

	@Transactional
	public String insertActivity(String uid, String period, Activity newActivity) {
		try {
//			ApiFuture<DocumentSnapshot> future = db.collection(uid).document(DocumentLabel.activity + "-" + period)
//					.get();
//			DocumentSnapshot document = future.get();

			CollectionReference cr = db.collection(uid);
			cr.document(DocumentLabel.activity).collection(period).document(newActivity.getId()).set(newActivity);

		} catch (Exception e) {
			return e.getMessage();
		}
		return null;
	}
	
	@Transactional
	public CollectionReference getActivityCollectionReference(String UID, String period) {
		return  db.collection(UID).document(DocumentLabel.activity).collection(period);
	}
	
	@Transactional
	public String updateUserInfo(UserUpdateRequest newUser) {
		try {
			UpdateRequest request = new UpdateRequest(newUser.getUid())
				    .setEmail(newUser.getEmail())
				    //.setPhoneNumber("+11234567890")
				    //.setEmailVerified(false)
				    .setPassword(newUser.getPassword())
				    .setDisplayName(newUser.getUsername())
				    .setPhotoUrl(newUser.getUrlAvatar())
				    //.setDisabled(true)
				    ;

			UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
			
			return null;

		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
}
