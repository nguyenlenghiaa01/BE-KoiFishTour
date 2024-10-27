package com.example.demo.model.Response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BookingsResponse {
    private long bookingId;
    private LocalDate startDate;
    private String status;
    private float price;
    private String duration;
    private String email;
    private String phone;
    private String fullName;
    private String address;
    private Date bookingDate;
    private int adult;
    private int child;
    private int infant;
    private long customerId;
    private String tourName;
    private String consultingName;
}
