package com.example.demo.model.Request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportRequest {


    @NotBlank
    private String reportType;
    @NotBlank
    private String reportContent;

}
