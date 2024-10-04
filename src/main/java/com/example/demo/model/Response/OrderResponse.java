package com.example.demo.model.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponse {

    private int quantity;

    private BigDecimal totalPrice;
}
