package com.example.demo.model.Request;

import lombok.Data;

@Data
public class ScheduleRequest {
    private String file;
    private String bookingId;
    private String customBookingId;
}
