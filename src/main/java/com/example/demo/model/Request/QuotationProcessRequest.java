package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import com.example.demo.entity.Quotation;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotationProcessRequest {

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    @Column(nullable = false) // Yêu cầu không null
    private String status;

    private String notes;

    long quotationId;

    long accountId;
}
