package com.example.demo.model.Response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OpenTourSearchResponse {
    private long id;
    private String tourId;
    private long saleId;
    private String tourName;
    private LocalDate startDate;
    private String duration;
    private String image;
    private String status;
    private double price;
    private double perAdultPrice;
    private double perChildrenPrice;
    private String time;
    private String description;
    private String schedule;
}
