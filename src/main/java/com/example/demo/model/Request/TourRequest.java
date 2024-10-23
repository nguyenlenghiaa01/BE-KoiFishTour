package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class TourRequest {

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\s].*", message = "First character not have space!")
    private String tourName;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotBlank(message = "Duration can not be blank")
    @Pattern(regexp = "^[2-5] days$", message = "Duration correct format!")
    private String duration;

    private List<String> image;
    private String description;
    private double price;
    private boolean isDeleted;
    @NotBlank(message = "Time cannot be blank")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "Invalid time format! Must be in HH:mm format.")
    private String time;
    private Long consultingId;
    Set<Long> farmId;
}
