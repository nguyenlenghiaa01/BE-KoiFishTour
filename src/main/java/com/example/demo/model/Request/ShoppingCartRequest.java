package com.example.demo.model.Request;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ShoppingCartRequest {
    private boolean status;
    private Set<Long> breedIds;
    private long customerId;
}
