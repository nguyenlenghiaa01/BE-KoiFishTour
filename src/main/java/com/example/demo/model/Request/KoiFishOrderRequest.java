package com.example.demo.model.Request;

import lombok.Data;

@Data
public class KoiFishOrderRequest {

    private long koiFishId;
    private double quantity;
    private float price;
    private long customerId;
    private long consultingId;
}
