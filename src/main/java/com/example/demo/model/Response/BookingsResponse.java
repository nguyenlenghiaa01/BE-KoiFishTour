package com.example.demo.model.Response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingsResponse {
    private LocalDate startDate;
    private String status;
    private float price;
    private String duration;
    private String email;
    private String phone;
    private String fullName;
    private String address;
    private LocalDate bookingDate;
    private int adult;
    private int child;
    private int infant;
    private long customerId;
    private String tourName;
}