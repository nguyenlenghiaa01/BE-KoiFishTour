package com.example.demo.model.Response;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.OpenTour;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class BookingForConsulting {
    private OpenTour openTour;
    private List<Booking> bookingOfOpenTour;
}
