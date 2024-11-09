package com.example.demo.model.Response;

import com.example.demo.entity.CustomBooking;
import com.example.demo.entity.Farm;
import com.example.demo.entity.Quotation;
import jakarta.mail.Quota;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class CustomBookingResponse {
    private long id;
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
    private String consultingName;
    private String saleName;
}
