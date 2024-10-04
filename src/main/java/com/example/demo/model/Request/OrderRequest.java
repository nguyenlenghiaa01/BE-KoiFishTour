package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import com.example.demo.entity.KoiFish;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;

@Data
public class OrderRequest {
    @Min(value = 10, message = "Quantity must be at least 10!")
    @Max(value = 1000, message = "Quantity must not be more than 1000!")
    private int quantity;

    @Min(value = 0, message = "Total price must be positive!")
    private BigDecimal totalPrice; // Đổi sang BigDecimal


    @ManyToMany
    @JoinTable(
            name = "order_koiFish",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "koiFish_id")
    )
    @JsonIgnore
    Set<KoiFish> koiFishes;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

}
