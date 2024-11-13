package com.example.demo.api;

import com.example.demo.entity.Booking;
import com.example.demo.entity.CustomBooking;
import com.example.demo.model.Request.CustomBookingRequest;
import com.example.demo.model.Request.CustomBookingRequests;
import com.example.demo.model.Response.CustomBookingResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.CustomBookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customBooking")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CustomBookingAPI {
    @Autowired
    CustomBookingService customBookingService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/VNPay")
    public ResponseEntity<?> createVNPay(String id) {
        String vnPayUrl = null;
        try {
            vnPayUrl = customBookingService.createUrl(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(vnPayUrl);
    }

    @PutMapping("/setStatusAfterPayment")
    public ResponseEntity<CustomBooking> updateStatus(String id){
        CustomBooking booking =customBookingService.updateStatus(id);
        simpMessagingTemplate.convertAndSend("topic/booking","UPDATE CUSTOM BOOKING");
        return  ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<CustomBooking>create(@Valid @RequestBody CustomBookingRequest customBookingRequests){
        CustomBooking customTour = customBookingService.createNewCusBooking(customBookingRequests);
        simpMessagingTemplate.convertAndSend("/topic/customBooking","CREATE NEW CUSTOM BOOKING");
        return ResponseEntity.ok(customTour);
    }

    @GetMapping("/get")
    public ResponseEntity<DataResponse<CustomBooking>>get(@RequestParam int page,@RequestParam int size ){
        DataResponse<CustomBooking>dataResponse = customBookingService.getAllBooking(page,size);
        return ResponseEntity.ok(dataResponse);
    }
    @GetMapping("customer-id")
    public ResponseEntity<DataResponse<CustomBookingResponse>>get(@RequestParam int page,@RequestParam int size,String id ){
        DataResponse<CustomBookingResponse> dataResponse = customBookingService.getAllCusBookingByCustomerId(page,size,id);
        return ResponseEntity.ok(dataResponse);
    }
    @GetMapping("customBooking-id")
    public ResponseEntity<CustomBooking> get(String id ){
        CustomBooking dataResponse = customBookingService.getCusBooking(id);
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomBooking> updateCustom(@Valid @RequestBody CustomBookingRequests customBookingRequests, @PathVariable long id) {
        CustomBooking cus = customBookingService.updateCus(customBookingRequests, id);
        simpMessagingTemplate.convertAndSend("/topic/customBooking","UPDATE CUSTOM BOOKING");
        return ResponseEntity.ok(cus);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<CustomBooking> deleteCustomBooking(@PathVariable long id) {
        CustomBooking customBooking = customBookingService.deleteCusBooking(id);
        return ResponseEntity.ok(customBooking);
    }
    @GetMapping("/idQuotation")
    public ResponseEntity<CustomBooking> get(long id){
        CustomBooking booking = customBookingService.getQuotation(id);
        return ResponseEntity.ok(booking);
    }


}
