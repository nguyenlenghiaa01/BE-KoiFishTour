package com.example.demo.model.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TourResponse {

    private String tourName;

    private LocalDate startDate;

    private String duration;
}
