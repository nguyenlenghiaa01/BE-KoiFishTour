package com.example.demo.model.Response;

import com.example.demo.entity.CustomTour;
import com.example.demo.entity.Farm;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CustomBookingResponses {
    private long customerId;
    private String cusBookingId;
    private long saleId;
    private LocalDate startDate;
    private String duration;
    private String email;
    private String phone;
    private String status;
    private String fullName;
    private String address;
    private int adult;
    private int child;
    private int infant;
    private double price;
    private Set<Farm> farm;
    private CustomTour customTour;
}
