package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.Random;

@Data
@Entity
public class CustomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "CBK\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String customBookingId;

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("CBK%07d", number);
    }

    private boolean isDeleted = false;
    private String status;

    private Date createAt;

    private double price;

    @OneToOne(mappedBy = "customBooking")
    @JsonIgnore
    private KoiFishOrder koiFishOrder;

    @OneToOne(mappedBy = "customBooking")
    private Quotation quotation;
    @OneToOne
    @JoinColumn(name = "customTour_id")
    private CustomTour customTour;

    @OneToOne(mappedBy = "customBooking")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "consulting_id")
    private Account consulting;
}
