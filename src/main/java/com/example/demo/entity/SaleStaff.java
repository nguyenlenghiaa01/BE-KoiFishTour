package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@Entity // Đánh dấu lớp này sẽ được lưu vào cơ sở dữ liệu
public class SaleStaff extends Account {
    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "SAL\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String saleId;
    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false) // Thêm tên cột và yêu cầu không null
    private Account account; // Sử dụng thực thể Account

    public SaleStaff() {
        this.saleId = generateSaleId(); // Tạo ID khi khởi tạo
    }

    private String generateSaleId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("SAL%07d", number); // Định dạng với 7 chữ số
    }
}
