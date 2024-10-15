package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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
    private boolean status = false;

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
            name = "shopping_cart_breed",
            joinColumns = @JoinColumn(name = "shopping_cart_id"),
            inverseJoinColumns = @JoinColumn(name = "breed_id")
    )
    @JsonIgnore
    private Set<Breed> breeds = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "koiFishOrder_id")
    @JsonIgnore
    private KoiFishOrder koiFishOrder;
}
