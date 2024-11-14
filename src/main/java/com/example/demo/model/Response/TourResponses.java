package com.example.demo.model.Response;

import com.example.demo.entity.Farm;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TourResponses {
    private long id;
    private String tourId;
    private boolean isDeleted;
    private String tourName;
    private LocalDate startDate;
    private String duration;
    private String image;
    private String status;
    private double price;
    private String time;
    private String description;
    private String schedule;
    private double perAdultPrice;
    private double perChildrenPrice;
    private String consultingName;
    private Set<Farm> farms;
}
