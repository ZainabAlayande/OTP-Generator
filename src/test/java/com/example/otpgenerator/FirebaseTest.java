package com.example.otpgenerator;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class FirebaseTest {

    @Autowired
    private Firestore firestore;


    @SneakyThrows
    @Test
    public void dataCanBePersistedTest() {
        DocumentReference docRef = firestore.collection("users").document("alovelace");

        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);

        ApiFuture<WriteResult> result = docRef.set(data);

        System.out.println("Update time : " + result.get().getUpdateTime());
    }


    @SneakyThrows
    @Test
    public void test() {
        DocumentReference docRef = firestore.collection("otp").document();

        Map<String, Object> data = new HashMap<>();
        data.put("otp", 345673);

        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }


    @SneakyThrows
    @Test
    public void addMoreDataToFirebaseTest() {
        DocumentReference docRef = firestore.collection("users").document("aturing");

        Map<String, Object> data = new HashMap<>();
        data.put("first", "Alan");
        data.put("middle", "Mathison");
        data.put("last", "Turing");
        data.put("born", 1912);

        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }


    @SneakyThrows
    @Test
    public void retrieveAllUserTest() {
        ApiFuture<QuerySnapshot> query = firestore.collection("users").get();

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
    }


}
