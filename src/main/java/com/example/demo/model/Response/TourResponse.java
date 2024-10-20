package com.example.demo.model.Response;

import com.example.demo.entity.Farm;
import com.example.demo.entity.OpenTour;
import com.example.demo.entity.Tour;
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
    private Set<Farm> farms;
}
