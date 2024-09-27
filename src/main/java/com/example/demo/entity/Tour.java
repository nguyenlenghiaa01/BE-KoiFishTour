package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@Entity
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "TOUR\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String tourId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\s].*", message = "First character not have space!")
    private String tourName;

    @NotBlank(message = "Start date can not be blank")
    private LocalDate startDate; // Sử dụng LocalDate thay vì Date

    @NotBlank(message = "Duration can not be blank")
    @Pattern(regexp = "^[2-5] days$", message = "Enter the correct format!")
    private String duration; // Chuyển sang String để dễ dàng kiểm tra định dạng

    @NotBlank(message = "Price can not be blank")
    @Pattern(regexp = "^(VND\\s?\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?|\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?\\s?VND)$", message = "Enter the correct format!")
    private String price; // Chuyển sang String để dễ dàng kiểm tra định dạng

    public Tour() {
        this.tourId = generateTourId(); // Tạo ID khi khởi tạo
    }

    private String generateTourId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("TOUR%07d", number); // Định dạng với 7 chữ số
    }

    @OneToMany(mappedBy = "tour")
    Set<OpenTour> openTours;

    @ManyToMany
    @JoinTable(
            name = "tour_farm",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "farm_id")
    )
    Set<Farm> farms;
}
