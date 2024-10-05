package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Data
@Entity
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "TOR\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String tourId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\s].*", message = "First character not have space!")
    private String tourName;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate; // Sử dụng LocalDate thay vì Date

    @NotBlank(message = "Duration can not be blank")
    @Pattern(regexp = "^[2-5] days$", message = "Enter the correct format!")
    private String duration; // Chuyển sang String để dễ dàng kiểm tra định dạng

    private String image;

    private String generateTourId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("TOR%07d", number); // Định dạng với 7 chữ số
    }
    @PrePersist
    private void prePersist() {
        this.tourId = generateTourId();
    }

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    Set<OpenTour> openTours;

    @ManyToMany
    @JoinTable(
            name = "tour_farm",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "farm_id")
    )
    Set<Farm> farms;
}
