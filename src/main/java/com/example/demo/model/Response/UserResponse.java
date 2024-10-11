package com.example.demo.model.Response;

import com.example.demo.entity.Role;
import lombok.Data;

@Data
public class UserResponse {
    private String token;
    private String email;  // Thêm trường email để trả về
    private String fullName;
    private String image;
    private Role role;
}
