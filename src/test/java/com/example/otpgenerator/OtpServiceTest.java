package com.example.otpgenerator;

import com.example.otpgenerator.exception.PhoneNumberNotVerifiedException;
import com.example.otpgenerator.service.otp.OtpService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class OtpServiceTest {

    @Autowired
    private OtpService otpService;

    @Test
    public void generateAndSaveOtpTest() {
        var response = otpService.generateOtp(6);
        log.info("Generated otp is: " + response.getOtp());
        assertThat(response).isNotNull();
        System.out.println("response => " + response);
    }


    @SneakyThrows
    @Test
    public void deleteByPhoneNumberOtpTest() {
        String otp = "";
        otpService.deleteByOtp(otp);
        PhoneNumberNotVerifiedException exception = assertThrows(PhoneNumberNotVerifiedException.class, () -> {
            otpService.findByOtp(otp);
        });
        assertEquals("PhoneNumber not verified", exception.getMessage());
    }


    @Test
    public void verifyOtpTest() {
        boolean isExist = otpService.verifyOtp("839698");
        assertTrue(isExist);
    }



}
