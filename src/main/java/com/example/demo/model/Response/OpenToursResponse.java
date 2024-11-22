package com.example.demo.model.Response;

import com.example.demo.entity.Farm;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class OpenToursResponse {
    private long id;
    private long tourId;
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
}
