package com.example.demo.model.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomTourResponse {
    private long id;
    private LocalDate startDate;

    private String duration;
    private String email;

    private String phone;

    private String fullName;

    private String address;
    private double budget;
    private int adult;
    private int child;
    private int infant;
    private List<String> farm;

}
