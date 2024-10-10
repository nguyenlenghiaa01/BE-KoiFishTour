package com.example.demo.api;

import com.example.demo.entity.Booking;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.model.Response.BookingResponse;
import com.example.demo.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("*")
@SecurityRequirement(name="api")
@PreAuthorize("hasAuthority('SALE')")
public class BookingAPI {
    @Autowired
    BookingService bookingService;
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingRequest bookingRequest) {
        Booking newBooking = bookingService.createNewBooking(bookingRequest);
        return ResponseEntity.ok(newBooking);
    }

    // Get danh sách breed
    @GetMapping
    public ResponseEntity<?> get(@RequestParam int page, @RequestParam int size){
        BookingResponse bookingResponse = bookingService.getAllBooking(page, size);
        return ResponseEntity.ok(bookingResponse);
    }
    // /api/booking/{id} => id cua thang booking minh muon update
    @PutMapping("{id}")
    public ResponseEntity<?> updateBooking(@Valid @RequestBody BookingRequest booking, @PathVariable long id){//valid kich hoat co che vadilation
        Booking newBooking = bookingService.updateBooking(booking,id);
        return ResponseEntity.ok(newBooking);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable long id){
        Booking newBooking = bookingService.deleteBooking(id);
        return ResponseEntity.ok(newBooking);
    }
}
