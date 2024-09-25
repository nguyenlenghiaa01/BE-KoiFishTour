package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

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

    @OneToOne
    @JoinColumn(name = "booking_id") // Thêm tên cột nếu cần thiết
    private Booking booking; // Thay đổi thành đối tượng Booking

    @ManyToOne
    @JoinColumn(name = "sale_id") // Thêm tên cột nếu cần thiết
    private Account account; // Thay đổi thành đối tượng Sale

    public Quotation() {
        this.quotationId = generateQuotationId(); // Tạo ID khi khởi tạo
    }

    private String generateQuotationId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("QUO%07d", number); // Định dạng với 7 chữ số
    }
}
