package com.example.demo.model.Response;

import com.example.demo.entity.Account;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class BookingForConsulting {
    private String bookingId;
    private String status;
    private double price;
    private Date bookingDate;
    private int adult;
    private int child;
    private int infant;
    private Account customer;
}
