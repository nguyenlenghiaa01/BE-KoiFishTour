package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {
    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000)); // Tạo OTP ngẫu nhiên từ 100000 đến 999999
        }
    }


