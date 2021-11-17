package com.mywallet.api.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.mywallet.api.config.DocumentLabel;
import com.mywallet.api.entity.User;
import com.mywallet.api.model.Activity;
import com.mywallet.api.response.Resp;
import com.mywallet.api.response.UserResponse;
import com.google.api.core.ApiFuture;

@Service
public class FireBaseService {

	@Autowired
    private FirebaseAuth firebaseAuth;
	
	@Autowired
	private Firestore db;

	@Transactional
	public Resp createUser(User baru) {
		Resp resp;
		try {
			System.out.println("Start created new user: ");
			CreateRequest request = new CreateRequest().setEmail(baru.getEmail()).setEmailVerified(false)
					.setPassword(baru.getPassword())
					// .setPhoneNumber(null)
					.setDisplayName(baru.getUsername())
					// .setPhotoUrl("http://www.example.com/12345678/photo.png")
					.setDisabled(false);
			//System.out.println("Start created new user: ");
			
			UserRecord userRecord = this.firebaseAuth.createUser(request);
			System.out.println("Successfully created new user: " + userRecord.getUid());
			
			resp = new Resp("success", null);
			resp.setData(new UserResponse(userRecord.getUid(), userRecord.getDisplayName(), userRecord.getEmail()));
			return resp;
		} catch (Exception e) {
			System.out.println("error : " + e.getMessage());
			resp = new Resp("Error", e.getMessage());
			return resp;
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
	
}
