package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Random;

@Getter
@Setter
@Entity
public class OrderCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "ORD\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String orderId;

    private boolean isDeleted = false;

    @Min(value = 10, message = "Quantity must be at least 10!")
    @Max(value = 1000, message = "Quantity must not be more than 1000!")
    private int quantity;

    @Min(value = 0, message = "Total price must be positive!")
    private BigDecimal totalPrice; // Đổi sang BigDecimal


    public OrderCart() {
        this.orderId = generateOrderId(); // Tạo ID khi khởi tạo
    }

    // Thay đổi phương thức để tránh trùng lặp mã đơn hàng
    private String generateOrderId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Thay đổi giới hạn thành 7 chữ số
        return String.format("ORD%07d", number); // Tạo mã có 7 chữ số
    }
}

