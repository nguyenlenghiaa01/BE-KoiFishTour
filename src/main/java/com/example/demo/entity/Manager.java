package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@Entity
public class Manager extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "MAN\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String managerId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id") // Giả sử 'id' là khóa chính trong Account
    private Account account;

    public String generateManagerId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("MAN%07d", number);
    }

    public Manager() {
        this.managerId = generateManagerId(); // Tạo ID khi khởi tạo
    }
}
