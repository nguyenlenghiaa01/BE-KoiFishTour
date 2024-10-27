package com.example.demo.model.Response;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class QuotationProcessResponse {
    private Date createdAt;
    private String status;
    private String notes;
    long quotationId;
    long accountId;
}
