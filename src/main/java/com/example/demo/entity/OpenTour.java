package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@Entity
public class OpenTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\s].*", message = "First character not have space!")
    private String tourName;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotBlank(message = "Duration can not be blank")
    @Pattern(regexp = "^[2-5] days$", message = "Enter the correct format!")
    private String duration;

    private String image;

    private String description;

    private String status;

    private double price;

    private String schedule;

    private String time;

    private double perAdultPrice;

    private double perChildrenPrice;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @OneToMany(mappedBy = "openTour")
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Account sale;

}
