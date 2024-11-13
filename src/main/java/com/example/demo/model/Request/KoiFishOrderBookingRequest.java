package com.example.demo.model.Request;

import com.example.demo.Enum.OrderEnum;
import lombok.Data;

import java.util.List;

@Data
public class KoiFishOrderBookingRequest {
    private List<Long> shoppingCart;
    private long customerId;
    private String customBookingId;
    private OrderEnum status;
    private double paidMoney;
    private double totalPrice;
}
