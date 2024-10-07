package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "CAR\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String cartId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "koi_fish_id")
    @JsonIgnore
    KoiFish koiFish;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @PrePersist
    private void prePersist() {
        this.cartId = generateBookingId();
    }

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("CAR%07d", number); // Định dạng với 7 chữ số
    }


}
