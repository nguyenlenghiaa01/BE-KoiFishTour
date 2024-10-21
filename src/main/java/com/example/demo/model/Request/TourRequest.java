package com.example.demo.model.Request;

import com.example.demo.entity.Farm;
import com.example.demo.entity.OpenTour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Data
public class TourRequest {

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\s].*", message = "First character not have space!")
    private String tourName;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate; // Sử dụng LocalDate thay vì Date

    @NotBlank(message = "Duration can not be blank")
    @Pattern(regexp = "^[2-5] days$", message = "Enter the correct format!")
    private String duration; // Chuyển sang String để dễ dàng kiểm tra định dạng

    private String image;
    private double price;
    private boolean isDeleted;
    @NotBlank(message = "Time cannot be blank")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "Invalid time format! Must be in HH:mm format.")
    private String time;

    Set<Long> farmId;
}
