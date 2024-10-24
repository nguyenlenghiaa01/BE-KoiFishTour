package com.example.demo.model.Request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class KoiFishOrderRequest {

    private List<Long> shoppingCart;
    private double quantity;
    private float price;
    private long customerId;
    private long consultingId;
}
