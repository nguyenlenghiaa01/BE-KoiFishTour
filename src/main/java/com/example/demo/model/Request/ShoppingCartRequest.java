package com.example.demo.model.Request;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ShoppingCartRequest {
    private String status;
    private Set<Long> koiFishId;

}
