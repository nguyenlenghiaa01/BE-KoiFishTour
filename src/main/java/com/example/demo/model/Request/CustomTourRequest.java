package com.example.demo.model.Request;

import com.example.demo.entity.Farm;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CustomTourRequest {
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
    private Set<String> farm;
}
