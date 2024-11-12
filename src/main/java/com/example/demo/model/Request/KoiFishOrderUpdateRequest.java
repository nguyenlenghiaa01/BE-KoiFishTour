package com.example.demo.model.Request;

import com.example.demo.Enum.OrderEnum;
import lombok.Data;

import java.util.List;

@Data
public class KoiFishOrderUpdateRequest {
    private List<Long> shoppingCart;
    private OrderEnum status;
    private double paidMoney;
    private double totalPrice;
}
