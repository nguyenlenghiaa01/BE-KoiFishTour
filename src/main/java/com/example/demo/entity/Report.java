package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @NotBlank
    private String reportType;

    private LocalDateTime createdDate;

    private String createdBy;

    @NotBlank
    private String reportContent;

    private boolean isDeleted = false;
}
