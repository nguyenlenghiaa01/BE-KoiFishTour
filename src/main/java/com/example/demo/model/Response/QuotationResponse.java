package com.example.demo.model.Response;

import lombok.Data;

@Data
public class QuotationResponse {
    private long bookingId;
    private double adultPrice;
    private double childPrice;
    private double totalPrice;
    private long quotationId;
}
