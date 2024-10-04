package com.example.demo.model.Response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotationProcessResponse {
    private LocalDateTime createdAt;

    private String status;

    private String notes;
}
