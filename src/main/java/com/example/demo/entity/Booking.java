package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "BOO\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String bookingId;

    private boolean isDeleted = false;


    @ManyToOne
    @JoinColumn(name = "saleId")
    private Account account;


    @ManyToOne
    @JoinColumn(name = "tourId")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name="open_tour_id")
    private OpenTour openTour;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date can not be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date startDate;

    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot have numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    @NotBlank(message = "Price cannot be blank")
    @Pattern(regexp = "^(VND)?\\s?\\d+(?:[.,]\\d{3})*(?:[.,]\\d{2})?$", message = "Enter the correct format!")
    private BigDecimal price;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date bookingDate;

    @PrePersist
    private void prePersist() {
        this.bookingId = generateBookingId();
    }

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("BOK%07d", number); // Định dạng với 7 chữ số
    }
}
