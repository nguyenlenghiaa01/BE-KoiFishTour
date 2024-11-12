package com.example.demo.model.Request;

import com.example.demo.entity.Farm;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class CustomTourRequest {
    @NotNull(message = "Start date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private String duration;
    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;
    @NotBlank(message = "Address cannot be blank")
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double budget;

    private String status;

    private int adult;
    private int child;
    private int infant;
    private Set<String> farm;
}
