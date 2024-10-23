package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingRequest {

    @NotNull(message = "Start date cannot be null")

    private LocalDate startDate; // Sử dụng LocalDate thay vì Date

    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot have numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    @NotNull(message = "Price cannot be null")
    private float price;


    private String duration;

    @NotNull(message = "Booking date cannot be null")
    private LocalDate bookingDate;

    @NotNull(message = "Consulting ID cannot be null")
    private long consultingId;
}

