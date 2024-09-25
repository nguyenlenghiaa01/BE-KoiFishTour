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
public class OrderFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "OFis\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String orFishId;


    @ManyToOne
    @JoinColumn(name = "koiFish_id")
    private KoiFish koiFish;

    @ManyToOne
    @JoinColumn(name = "order_cart_id")
    private OrderCart orderCart;


    public OrderFish() {
        this.orFishId = generateOrFishId(); // Tạo ID khi khởi tạo
    }

    private String generateOrFishId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("OFis%07d", number); // Định dạng với 7 chữ số
    }
}
