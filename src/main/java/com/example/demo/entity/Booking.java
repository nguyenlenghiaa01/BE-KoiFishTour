package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Pattern(regexp = "^[^\\d\\s].*", message = "Status cannot contain numbers and first character cannot be space!")
    private String status;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private float price;

    @NotNull(message = "End date cannot be null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    private Date bookingDate;

    @PrePersist
    private void prePersist() {
        this.bookingId = generateBookingId();
    }

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("BOK%07d", number);
    }

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private List<Feedback> feedbacks;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "consulting")
    private Account consulting;


    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    Payment payment;


}
