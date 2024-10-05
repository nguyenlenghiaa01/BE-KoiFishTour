package com.example.demo.model.Request;

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
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format, max 10 digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "Booking date cannot be null")
    private LocalDate bookingDate;

    private long openTourId;
    private long accountId;
}

