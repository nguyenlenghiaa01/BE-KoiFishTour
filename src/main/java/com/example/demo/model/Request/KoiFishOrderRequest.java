package com.example.demo.model.Request;

import com.example.demo.Enum.OrderEnum;
import lombok.Data;
import org.hibernate.query.Order;

import java.util.List;
import java.util.Set;

@Data
public class KoiFishOrderRequest {

    private List<Long> shoppingCart;
    private long customerId;
    private String bookingId;
    private double paidMoney;
    private double totalPrice;
}
