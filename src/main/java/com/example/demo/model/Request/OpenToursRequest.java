package com.example.demo.model.Request;

import lombok.Data;

import java.time.LocalDate;


@Data
public class OpenToursRequest {
    private long tourId;
    private String schedule;
    private double price;
    private LocalDate startDate;
}
