package com.example.demo.model;

import com.example.demo.entity.Role;
import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String code;
    String userName;
    String fullName;
    String phone;
    String email;
//    String address;
    Role role;
    String token;
}
