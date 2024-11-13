package com.example.demo.model.Response;

import com.example.demo.Enum.QuotationEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotationCancelResponse {
    private String customBookingId;
    private double adultPrice;
    private double childPrice;
    private double totalPrice;
    private long quotationId;
    private QuotationEnum status;
    private LocalDateTime createAt;
    private String saleName;
    private String fullName;
    private String phone;
    private String email;
    private double priceTour;
}
