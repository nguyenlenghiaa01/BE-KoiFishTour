package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private KoiFish koiFish;

    @ManyToOne
    @JoinColumn(name="koi_fish_order_id")
    private KoiFishOrder koiFishOrder;

}
