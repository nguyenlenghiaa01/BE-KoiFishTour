package com.example.demo.model;

import com.example.demo.entity.Role;
import lombok.Data;

@Data
public class AccountResponse {
    String code;
    String userName;
    String phone;
    String email;
    String token;
    Role role;
}
