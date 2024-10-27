package com.example.demo.model.Request;

import com.example.demo.Enum.QuotationEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class QuotationRequest {
    long bookingId;
    private double perAdultPrice;
    private double perChildPrice;
}
