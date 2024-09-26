package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "MAN\\d{6}", message = "Invalid code!")
    @Column(unique = true)
    String code;
    @Column(nullable = false) // Đảm bảo không null

    private boolean isDeleted = false;

    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;


    @Size(min = 6, message = "Password must be at least 6 character!")
    @Column(unique = true)
    private String password;

}
