package com.example.otpgenerator.service.otp;

import com.example.otpgenerator.dtos.response.OtpResponse;
import com.example.otpgenerator.exception.OtpException;
import com.example.otpgenerator.models.Otp;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.example.otpgenerator.service.otp.OtpUtils.*;


@Service
public class OtpServiceImpl implements OtpService {

    private final Firestore firestore;


    public OtpServiceImpl(Firestore firestore) {
        this.firestore = firestore;
    }


    @SneakyThrows
    @Override
    public OtpResponse generateOtp(int length){
        String otp = generateOneTimePassword(length);
        CollectionReference otpCollection = firestore.collection("otp");
        ApiFuture<DocumentReference> savedOtp = otpCollection.add(Otp.builder().otp(otp).createdAt(LocalDateTime.now()).build());
        String result = savedOtp.get().getId();
        return OtpResponse.builder().otp(otp).build();
    }


    @SneakyThrows
    public boolean verifyOtp(String otp)  {
        CollectionReference otpCollection = firestore.collection("otp");

        // Build a query to find the document by phoneNumber
        Query query = otpCollection.whereEqualTo("otp", otp);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Otp foundOtp = document.toObject(Otp.class);

            // Verify the OTP
            if (foundOtp.getOtp().equals(otp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SneakyThrows
    public void deleteByOtp(String phoneNumber)  {
        CollectionReference otpCollection = firestore.collection("otp");

        // Build a query to find the document by phoneNumber
        Query query = otpCollection.whereEqualTo("phoneNumber", phoneNumber);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            // Delete the document
            document.getReference().delete();
        }
    }

    @Override
    @SneakyThrows
    public Otp findByOtp(String otp) {
        CollectionReference otpCollection = firestore.collection("otp");

        Query query = otpCollection.whereEqualTo("otpNumber", otp);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            return document.toObject(Otp.class);
        }
        throw new OtpException(OTP_SEARCH_FAILED);
    }


    @Override
    @SneakyThrows
    public List<Otp> findAllOtp()  {
        CollectionReference otpCollection = firestore.collection("otp");

        ApiFuture<QuerySnapshot> querySnapshot = otpCollection.get();
        List<Otp> otpList = querySnapshot.get().toObjects(Otp.class);
        return otpList;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void handleOtpTimeoutScheduled() {
        handleOtpTimeout();
    }

    static String generateOneTimePassword(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return otp.toString();
    }

    @SneakyThrows
    private void handleOtpTimeout() {
        var allOtp = findAllOtp();
        for (Otp each:allOtp) {
            if((Duration.between(LocalDateTime.now(),each.getCreatedAt()).toMinutes()% 60) > 5){
                deleteByOtp(each.getOtp());
            }
        }
    }

}
