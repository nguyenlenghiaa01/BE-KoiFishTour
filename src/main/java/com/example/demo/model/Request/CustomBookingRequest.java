package com.example.demo.model.Request;

import lombok.Data;

@Data
public class CustomBookingRequest {
    private long customTourId;
    private String status;
    private long consultingId;
    private double price;
}
