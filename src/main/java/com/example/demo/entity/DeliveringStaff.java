package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@Entity // Đánh dấu lưu xuống DB
public class DeliveringStaff extends Account {
    @Id // Đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "DEL\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String deliveringId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "account_id") // Đặt tên cột cho khóa ngoại
    private Account account; // Thay đổi từ Long thành Account

    @PrePersist
    private void prePersist() {
        this.deliveringId = generateDeliveringId();
    }

    private String generateDeliveringId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("DELI%07d", number); // Định dạng với 7 chữ số
    }
}
