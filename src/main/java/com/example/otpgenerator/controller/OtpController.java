package com.example.otpgenerator.controller;

import com.example.otpgenerator.dtos.response.ApiResponse;
import com.example.otpgenerator.dtos.response.OtpResponse;
import com.example.otpgenerator.service.otp.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@Slf4j
public class OtpController {

    public final OtpService otpService;


    @RequestMapping(
            method = RequestMethod.POST,
            value = "/generateOtp/{length}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ApiResponse<?>> generateOtp(@PathVariable @Positive int length) {
        try {
            OtpResponse response = otpService.generateOtp(length);
            log.info("Generated OTP successfully for length: {}", length);
            return ResponseEntity.ok().body(ApiResponse.builder().data(response).message("Generated").success(true).build());
        } catch (Exception exception) {
            log.error("Failed to generate OTP for length: {}", length, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder().message("Failed to generate").success(false).build());
        }
    }



    @RequestMapping(
            method = RequestMethod.POST,
            value = "/verifyOtp/{otp}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ApiResponse<?>> verifyOtp(@PathVariable String otp)  {
        boolean isVerified = false;
        try {
            isVerified = otpService.verifyOtp(otp);
            return ResponseEntity.ok().body(ApiResponse.builder().data(isVerified).message("Verified").build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder().data(isVerified).message("Verified").build());
        }
    }



}
