package com.example.demo.model;

import com.example.demo.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CustomerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "[A-Z]{3}\\d{6}", message = "Invalid code!")
    @Column(unique = true)
    String code;

    private boolean isDeleted =false;

    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "UserName cannot be blank")
    @Pattern(regexp = "^\\S+$", message = "username cannot have space!")
    @Column(name="user_name",unique = true)
    private String userName;

    @Size(min = 6, message = "Password must be at least 6 character!")
    @Column(unique = true)
    private String password;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
