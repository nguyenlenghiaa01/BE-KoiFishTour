package com.example.demo.model.Request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class KoiFishOrderRequest {

    private List<Long> shoppingCart;
    private long customerId;
    private long bookingId;
    private String status;
    private float paidMoney = 0;
    private double totalPrice = 0;
}
