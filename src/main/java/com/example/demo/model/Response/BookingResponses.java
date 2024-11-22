package com.example.demo.model.Response;

import com.example.demo.entity.Tour;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponses {
    private String bookingId;
    private LocalDate startDate;
    private String status;
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
    private OpenToursResponse tourId;
}
