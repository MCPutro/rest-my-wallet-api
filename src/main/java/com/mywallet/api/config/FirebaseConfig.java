package com.mywallet.api.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

import java.util.Random;

@Configuration
public class FirebaseConfig {

	private final Environment env;

	@Autowired
	public FirebaseConfig(Environment env) {
		this.env = env;
	}

	@Bean
	public FirebaseAuth firebaseAuth() {
		return FirebaseAuth.getInstance();
	}

	@Bean
	public Firestore firestore() {
		return FirestoreClient.getFirestore();
	}
	
	@Bean
	public Random random() {
		return new Random();
	}

	@PostConstruct
	public void initializeFirebaseApp() {
		try {
			String server = env.getProperty("appServer");
			String key;
			if (server == null) {
				URL url = Resources.getResource("firebase-key.json");
				key = Resources.toString(url, Charsets.UTF_8);
				System.out.println("dari file json");
			} else {
				//load key from env variable
				key = env.getProperty("firebase_key");
				System.out.println("bukan dari file json");
			}

			InputStream serviceAccount = new ByteArrayInputStream(key.getBytes());

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					// .setServiceAccountId("firebase-adminsdk-2d4cb@my-wallet-api-93eaa.iam.gserviceaccount.com")
					.build();
			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			System.out.println("FirebaseConfig|initializeFirebaseApp|" + LocalDateTime.now() + "|" + e.getMessage());
		}

	}
}
