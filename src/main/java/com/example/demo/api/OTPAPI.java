package com.example.demo.api;

import com.example.demo.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OTPAPI {


//        @Autowired
//        private EmailService emailService;
//
//        @PostMapping("/send-otp")
//        public String sendOtp(@RequestParam String email) {
//            // Tạo mã OTP ngẫu nhiên (ví dụ: từ 100000 đến 999999)
//            String otp = String.valueOf(100000 + (int)(Math.random() * 900000));
//
//            // Gửi OTP qua email
//            emailService.sendOtp(email, otp);
//
//            return "OTP has been sent to " + email;
//        }
    }


