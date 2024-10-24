package com.example.demo.model.Response;

import com.example.demo.entity.Account;
import com.example.demo.entity.Farm;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class TourResponse {
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
    private long consultingId;
    private Set<Farm> farms;
}
