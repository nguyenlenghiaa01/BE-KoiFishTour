package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingRequest {

    @NotNull(message = "Start date cannot be null")

//    private LocalDate startDate; // Sử dụng LocalDate thay vì Date

    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot have numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    @NotNull(message = "Price cannot be null")
    private float price;


//    private String duration;
    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;

//    @NotBlank(message = "Address cannot be blank")
//    private String address;
    private int adult;
    private int child;
    private int infant;


//    private LocalDate bookingDate;
    private long customerId;
    private long tourId;
}

