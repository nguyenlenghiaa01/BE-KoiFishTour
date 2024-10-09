package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
public class OpenTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "OPT\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String openTourId;

    @PrePersist
    private void prePersist() {
        this.openTourId = generateBookingId();
    }

    private String generateBookingId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 9999999
        return String.format("OPT%07d", number); // Định dạng với 7 chữ số
    }

    @Column(nullable = false)
    private boolean isDeleted = false; // Mặc định là không bị xóa

    @Min(value = 0, message = "Total price must be positive!")
    @Column(nullable = false) // Đảm bảo totalPrice không được null
    private BigDecimal totalPrice; // Đổi sang BigDecimal

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    @Column(nullable = false) // Đảm bảo status không được null
    private String status;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false) // Đảm bảo liên kết với Tour không được null
    private Tour tour;

    @OneToMany(mappedBy = "openTour")
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;
}

