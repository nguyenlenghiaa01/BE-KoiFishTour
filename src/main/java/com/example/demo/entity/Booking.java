package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "BOK\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String bookingId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Pattern(regexp = "^[^\\d\\s].*", message = "Status cannot contain numbers and first character cannot be space!")
    private String status;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    private Date bookingDate;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;

    @Email(message = "Invalid Email!")
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    private String phone;

    private int adult;
    private int child;
    private int infant;


    @PrePersist
    private void prePersist() {
        this.bookingId = generateBookingId();
    }

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("BOK%07d", number);
    }

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    Payment payment;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    private KoiFishOrder koiFishOrder;


    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    private Schedule schedule;



}
