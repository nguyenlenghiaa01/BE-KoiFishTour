package com.example.demo.model.Response;

import com.example.demo.entity.Booking;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class BookingResponse {

    private long bookingId;
    private LocalDate startDate;
    private String status;
    private double price;
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
    private long openTourId;
}
