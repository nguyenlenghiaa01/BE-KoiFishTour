package com.example.demo.model.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenTourResponse {
    private BigDecimal totalPrice;
    private String status;
}
