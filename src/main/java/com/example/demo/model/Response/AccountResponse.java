package com.example.demo.model.Response;

import com.example.demo.Enum.Role;
import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String code;
    String userName;
    String fullName;
    String phone;
    String email;
    String address;
    Role role;
    String token;
    String image;
}
