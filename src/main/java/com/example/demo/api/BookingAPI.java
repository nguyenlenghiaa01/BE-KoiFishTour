package com.example.demo.api;

import com.example.demo.entity.Booking;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class BookingAPI {
    @Autowired
    BookingService bookingService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody BookingRequest bookingRequest) {
        Booking newBooking = bookingService.createNewBooking(bookingRequest);
        return ResponseEntity.ok(newBooking);
    }

    // Get danh sách breed
    @GetMapping
    public ResponseEntity get(){
        List<Booking> bookings = bookingService.getAllBooking();
        return ResponseEntity.ok(bookings);
    }
    // /api/booking/{id} => id cua thang boking minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateBooking(@Valid @RequestBody BookingRequest booking, @PathVariable long id){//valid kich hoat co che vadilation
        Booking newBooking = bookingService.updateBooking(booking,id);
        return ResponseEntity.ok(newBooking);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteBooking(@PathVariable long id){
        Booking newBooking = bookingService.deleteBooking(id);
        return ResponseEntity.ok(newBooking);
    }
}
