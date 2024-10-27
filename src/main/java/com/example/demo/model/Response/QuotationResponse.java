package com.example.demo.model.Response;

import com.example.demo.Enum.QuotationEnum;
import lombok.Data;

@Data
public class QuotationResponse {
    private long bookingId;
    private double adultPrice;
    private double childPrice;
    private double totalPrice;
    private long quotationId;
    private QuotationEnum status;
}
