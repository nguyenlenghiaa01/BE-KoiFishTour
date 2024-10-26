package com.example.demo.entity;

import com.example.demo.Enum.QuotationEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "QUO\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String quotationId;

    @PrePersist
    private void prePersist() {
        this.quotationId = generateQuotationId(); // Sử dụng hàm để tạo mã duy nhất
        this.uploadAt = LocalDateTime.now(); // Gán thời gian upload khi lưu vào DB
    }

    public String generateQuotationId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 9999999
        return String.format("QUO%07d", number); // Định dạng với 7 chữ số
    }

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "upload_at", nullable = false)
    private LocalDateTime uploadAt;

    @Enumerated(EnumType.STRING)
    private QuotationEnum status;

    @OneToMany(mappedBy = "quotation")
    @JsonIgnore
    private List<QuotationProcess> quotationProcesses;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;


}
