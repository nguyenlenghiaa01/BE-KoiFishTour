package com.example.demo.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Size(min = 6, message = "Password must be at least 6 character!")
    String password;

}
