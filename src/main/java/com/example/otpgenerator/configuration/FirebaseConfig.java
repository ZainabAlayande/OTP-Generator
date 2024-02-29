package com.example.otpgenerator.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore initializeFirestore() {
        try {
            // Use a service account
            InputStream serviceAccount = null;
            try {
                // Replace "path/to/serviceAccount.json" with the actual path to your service account JSON file
                serviceAccount = new FileInputStream("C:\\Users\\ADMIN\\IdeaProjects\\OTPGenerator\\src\\main\\resources\\serviceAccount.json");

                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);

                return FirestoreClient.getFirestore();
            } catch (IOException exception) {
                throw new RuntimeException("Error initializing Firebase", exception);
            } finally {
                if (serviceAccount != null) {
                    try {
                        serviceAccount.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }


    @Bean
    public CollectionReference otpCollection(Firestore firestore) {
        return firestore.collection("otp");
    }
}
