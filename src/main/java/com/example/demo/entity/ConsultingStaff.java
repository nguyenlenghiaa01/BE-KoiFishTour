package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity // Đánh dấu lưu xuống DB
public class ConsultingStaff extends Account {
    @Id // Đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thêm chiến lược tự động tăng
    private long id; // Sử dụng Long cho khóa chính

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "CON\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String consultingId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "account_id") // Đặt tên cột cho khóa ngoại
    private Account account; // Thay đổi từ Long thành Account

    @OneToMany(mappedBy = "consultingStaff") // Thêm mappedBy để chỉ định mối quan hệ
    private List<OrderCart> orders; // Sử dụng List cho mối quan hệ OneToMany

    @PrePersist
    private void prePersist() {
        this.consultingId = generateConsultingId();
    }

    private String generateConsultingId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("CON%07d", number); // Định dạng với 7 chữ số
    }
}
