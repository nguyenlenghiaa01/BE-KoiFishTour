package com.example.demo.model.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KoiFishOrderTotalRequest {
    @NotNull(message = "Month cannot be null")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year cannot be null")
    @Min(value = 1900, message = "Year must be greater than 1900")
    private Integer year;
}
