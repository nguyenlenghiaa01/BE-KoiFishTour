package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code cannot be blank!")
    @Pattern(regexp = "CAR\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String cartId;

    private boolean isDeleted = false;
    private String status;

    @PrePersist
    private void prePersist() {
        this.cartId = generateCartId();
    }

    private String generateCartId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("CAR%07d", number);
    }

    @ManyToMany
    @JoinTable(
            name = "order_koi_fish",
            joinColumns = @JoinColumn(name = "koi_fish_order_id"),
            inverseJoinColumns = @JoinColumn(name = "koi_fish_id")
    )
    private Set<KoiFish> koiFishes;

    @ManyToOne
    @JoinColumn(name = "koiFishOrder_id")
    @JsonIgnore
    private KoiFishOrder koiFishOrder;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Account customer;
}
