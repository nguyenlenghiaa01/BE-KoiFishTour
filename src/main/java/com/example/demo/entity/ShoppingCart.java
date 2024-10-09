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

    @PrePersist
    private void prePersist() {
        this.cartId = generateCartId();
    }

    private String generateCartId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("CAR%07d", number);
    }

    @ManyToOne
    @JoinColumn(name = "koiFish_id")
    private KoiFish koiFish;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderCart order;

}
