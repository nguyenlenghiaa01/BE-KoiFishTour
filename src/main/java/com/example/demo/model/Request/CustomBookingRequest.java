package com.example.demo.model.Request;

import lombok.Data;

@Data
public class CustomBookingRequest {
    private long customTourId;
    private long consultingId;
    private double price;
}
