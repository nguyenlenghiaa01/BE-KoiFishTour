package com.example.demo.model;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Tour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.Set;

public class OpenTourRequest {
    @Min(value = 0, message = "Total price must be positive!")
    private BigDecimal totalPrice; // Đổi sang BigDecimal

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    Tour tour;

    @OneToMany(mappedBy = "openTour")
    @JsonIgnore
    Set<Booking> bookings;
}
