package com.example.demo.model.Response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResponse {
    private Long reportId;

    private String reportType;

    private LocalDateTime createdDate;

    private String createdBy;

    private String reportContent;
}
