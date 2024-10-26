package com.example.demo.model.Request;

import com.example.demo.Enum.QuotationEnum;
import com.example.demo.entity.Account;
import com.example.demo.entity.Quotation;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotationProcessRequest {

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private QuotationEnum status;

    private String notes;

    long quotationId;

    long accountId;
}
