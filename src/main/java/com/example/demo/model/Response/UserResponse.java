package com.example.demo.model.Response;

import com.example.demo.Enum.Role;
import lombok.Data;

@Data
public class UserResponse {
    private String token;
    private String email;
    private String fullName;
    private String image;
    private Role role;
}
