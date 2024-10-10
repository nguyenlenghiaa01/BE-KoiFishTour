package com.example.demo.model.Response;

import com.example.demo.entity.KoiFishOrder;
import lombok.Data;

import java.util.List;

@Data

public class OrderResponse {
    private List<KoiFishOrder> listKoiFishOrder;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
