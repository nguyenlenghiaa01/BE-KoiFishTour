package com.example.demo.model.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;

    @Size(min = 6, message = "Password must be at least 6 character!")
    private String newPassword;


}
