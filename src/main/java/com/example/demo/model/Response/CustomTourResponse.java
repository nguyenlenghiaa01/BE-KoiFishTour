package com.example.demo.model.Response;

import com.example.demo.entity.Farm;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class CustomTourResponse {
    private long id;
    private LocalDate startDate;

    private String duration;
    private String email;

    private String phone;
    private String status;
    private String fullName;
    private String customerName;
    private String address;
    private double budget;
    private int adult;
    private int child;
    private int infant;
    private Set<Farm> farms;

}
