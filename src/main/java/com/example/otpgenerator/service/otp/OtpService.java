package com.example.otpgenerator.service.otp;


import com.example.otpgenerator.dtos.response.OtpResponse;
import com.example.otpgenerator.exception.OtpException;
import com.example.otpgenerator.exception.PhoneNumberNotVerifiedException;
import com.example.otpgenerator.models.Otp;

import java.util.List;

public interface OtpService {

    OtpResponse generateOtp(int length);

    boolean verifyOtp(String otp);

    void deleteByOtp(String otp);

    Otp findByOtp(String otp);

    List<Otp> findAllOtp();

}
