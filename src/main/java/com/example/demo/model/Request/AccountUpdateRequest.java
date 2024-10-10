package com.example.demo.model.Request;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    String email;
    String phone;
    String password;
    String fullName;
    String address;
    String image;
}
