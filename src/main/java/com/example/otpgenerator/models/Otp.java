package com.example.otpgenerator.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Otp {


    private String otp;
    private LocalDateTime createdAt;

}
