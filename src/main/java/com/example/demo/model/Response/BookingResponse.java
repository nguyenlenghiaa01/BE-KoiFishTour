package com.example.demo.model.Response;

import com.example.demo.entity.Booking;
import lombok.Data;

import java.util.List;

@Data
public class BookingResponse {
    private List<Booking> listBooking;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
