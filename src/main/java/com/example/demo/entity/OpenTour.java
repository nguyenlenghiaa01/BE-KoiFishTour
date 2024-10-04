package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class OpenTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean isDeleted= false;

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
    List<Booking> bookings;
}
