package com.example.demo.model.Response;

import com.example.demo.entity.OrderCart;
import lombok.Data;

import java.util.List;

@Data

public class OrderResponse {
    private List<OrderCart> listOrderCart;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
