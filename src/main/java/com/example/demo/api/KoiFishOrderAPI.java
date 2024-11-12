package com.example.demo.api;


import com.example.demo.entity.KoiFish;
import com.example.demo.entity.KoiFishOrder;

import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Request.KoiFishOrderUpdateRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.service.KoiFishOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFishOrderAPI {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private KoiFishOrderService koiFishOrderService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody KoiFishOrderRequest koiFishOrderRequest) {
        KoiFishOrder koiFishOrder = koiFishOrderService.create(koiFishOrderRequest);
        simpMessagingTemplate.convertAndSend("topic/koiOrder","CREATE NEW ORDER");
            return ResponseEntity.ok(koiFishOrder);
    }

    @GetMapping("customer/{id}")
    public ResponseEntity getCustomerOrder(@PathVariable long id, @RequestParam int page, @RequestParam int size){
        OrderResponse orders = koiFishOrderService.getCustomerOrder(id, page, size);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody KoiFishOrderUpdateRequest koiFishOrderRequest, @PathVariable long id) {
            KoiFishOrder updatedOrder = koiFishOrderService.updateOrder(koiFishOrderRequest, id);
            return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasAuthority('CONSULTING')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {

            KoiFishOrder deletedOrder = koiFishOrderService.deleteOrderCart(id);
            return ResponseEntity.ok(deletedOrder);
    }

    @GetMapping("booking/{id}")
    public ResponseEntity getOrderByBookingId(@PathVariable String id) {
        KoiFishOrder order = koiFishOrderService.getOrderByBookingId(id);
        return ResponseEntity.ok(order);
    }

}
