package com.example.demo.model.Response;

import com.example.demo.Enum.QuotationEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class QuotationResponse {
    private long bookingId;
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
}
