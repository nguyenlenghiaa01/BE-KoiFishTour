package com.example.demo.api;

import com.example.demo.entity.Booking;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.model.Response.*;
import com.example.demo.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class BookingAPI {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    BookingService bookingService;
    @PostMapping("/VNPay")
    public ResponseEntity<?> createVNPay(String id) {
        String vnPayUrl = null;
        try {
            vnPayUrl = bookingService.createUrl(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(vnPayUrl);
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest bookingRequest) {
        Booking newBooking =bookingService.createNewBooking(bookingRequest);
        simpMessagingTemplate.convertAndSend("/topic/booking","CREATE NEW BOOKING");
        return ResponseEntity.ok(newBooking);
    }

    @GetMapping
    public ResponseEntity<DataResponse<BookingsResponse>> get(@RequestParam int page,
                                                              @RequestParam int size) {
        DataResponse<BookingsResponse> bookingResponse = bookingService.getAllBooking(page, size);
        return ResponseEntity.ok(bookingResponse);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> create(@RequestParam String id) throws Exception{
        bookingService.createTransactionId(id);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/customerId")
    public ResponseEntity<DataResponse<BookingResponses>> getCustomerBooking(@RequestParam int page,
                                                                             @RequestParam int size,
                                                                             @RequestParam String id) {
        DataResponse<BookingResponses> bookingResponse = bookingService.getBookingByCustomer(page, size,id);
        return ResponseEntity.ok(bookingResponse);
    }

    @PutMapping("/setStatusAfterPayment")
    public ResponseEntity<Booking> updateStatus(String id){
        Booking booking =bookingService.updateStatus(id);
        simpMessagingTemplate.convertAndSend("topic/booking","UPDATE BOOKING");
        return  ResponseEntity.ok(booking);
    }

    @PutMapping("/endTour")
    public String endTour(@RequestParam long tourId){
        return bookingService.handleEndTour(tourId);
    }

    @GetMapping("/history")
    public ResponseEntity<DataResponse<BookingResponse>> getHistory(@RequestParam int page,
                                                                    @RequestParam int size,
                                                                    @RequestParam long id) {
        DataResponse<BookingResponse> bookingResponse = bookingService.getAllBookingCustomer(page, size, id);
        return ResponseEntity.ok(bookingResponse);
    }
    @GetMapping("/Consulting")
    public ResponseEntity<DataResponse<BookingForConsulting>> getBooking(@RequestParam int page,
                                                                    @RequestParam int size,
                                                                    @RequestParam long tourId) {
        DataResponse<BookingForConsulting> bookingResponse = bookingService.getBookingByConsultingTour(page, size, tourId);
        return ResponseEntity.ok(bookingResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable String id){
        Booking newBooking = bookingService.deleteBooking(id);
        simpMessagingTemplate.convertAndSend("topic/booking","DELETE BOOKING");
        return ResponseEntity.ok(newBooking);
    }

    @DeleteMapping("delete/bookingId")
    public ResponseEntity<Booking> delete(String id){
        Booking newBooking = bookingService.deleteBookings(id);
        return ResponseEntity.ok(newBooking );
    }

}
