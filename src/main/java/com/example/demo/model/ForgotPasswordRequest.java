package com.example.demo.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(message = "Invalid Email!")
    String email;
}
