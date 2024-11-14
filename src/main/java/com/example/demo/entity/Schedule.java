package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String file;

    private Date createAt;
    boolean isDeleted = false;


    @OneToOne
    @JoinColumn(name = "custom_booking_id")
    private CustomBooking customBooking;

    @ManyToOne
    @JoinColumn(name = "consulting_id")
    private Account consulting;


}
