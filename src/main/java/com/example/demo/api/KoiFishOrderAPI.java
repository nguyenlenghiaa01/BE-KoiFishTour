package com.example.demo.api;


import com.example.demo.entity.KoiFish;
import com.example.demo.entity.KoiFishOrder;

import com.example.demo.model.Request.KoiFishOrderBookingRequest;
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

import java.util.Date;
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

    @PreAuthorize("hasAuthority('CONSULTING')")
    @PostMapping
    public ResponseEntity<KoiFishOrder> create(@Valid @RequestBody KoiFishOrderRequest koiFishOrderRequest) {
        KoiFishOrder koiFishOrder = koiFishOrderService.create(koiFishOrderRequest);
        simpMessagingTemplate.convertAndSend("topic/koiOrder","CREATE NEW ORDER");
            return ResponseEntity.ok(koiFishOrder);
    }
    @PreAuthorize("hasAuthority('CONSULTING')")
    @PostMapping("order-customer")
    public ResponseEntity<KoiFishOrder> createCus(@Valid @RequestBody KoiFishOrderBookingRequest koiFishOrderRequest) {
        KoiFishOrder koiFishOrder = koiFishOrderService.createCus(koiFishOrderRequest);
        simpMessagingTemplate.convertAndSend("topic/koiOrder","CREATE NEW ORDER");
        return ResponseEntity.ok(koiFishOrder);
    }


    @PreAuthorize("hasAuthority('CONSULTING')")
    @PutMapping("{id}")
    public ResponseEntity<KoiFishOrder> updateOrder(@Valid @RequestBody KoiFishOrderUpdateRequest koiFishOrderRequest, @PathVariable long id) {
            KoiFishOrder updatedOrder = koiFishOrderService.updateOrder(koiFishOrderRequest, id);
            return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasAuthority('CONSULTING')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {

            KoiFishOrder deletedOrder = koiFishOrderService.deleteOrderCart(id);
            return ResponseEntity.ok(deletedOrder);
    }
    @PreAuthorize("hasAuthority('CONSULTING','CUSTOMER','SALE')")
    @GetMapping("customerBooking-id/{id}")
    public ResponseEntity<KoiFishOrder> getOrderByCustomerBookingId(@PathVariable String id) {
        KoiFishOrder order = koiFishOrderService.getOrderByCustomBookingId(id);
        return ResponseEntity.ok(order);
    }
    @PreAuthorize("hasAuthority('CONSULTING','CUSTOMER','SALE')")
    @GetMapping("booking/{id}")
    public ResponseEntity<KoiFishOrder> getOrderByBookingId(@PathVariable String id) {
        KoiFishOrder order = koiFishOrderService.getOrderByBookingId(id);
        return ResponseEntity.ok(order);
    }
    @PreAuthorize("hasAuthority('CONSULTING','CUSTOMER','SALE')")
    @GetMapping("{orderId}")
    public ResponseEntity<KoiFishOrder> getOrderByOrderId(@PathVariable String orderId) {
        KoiFishOrder order = koiFishOrderService.getOrderByOrderId(orderId);

        return ResponseEntity.ok(order);
    }
    @PreAuthorize("hasAuthority('CONSULTING','SALE')")
    @GetMapping("all")
    public ResponseEntity<OrderResponse> getAll(@RequestParam int page, @RequestParam int size) {
        OrderResponse order = koiFishOrderService.getAllOrder(page,size);
        return ResponseEntity.ok(order);
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("confirm/{id}")
    public ResponseEntity updateConfirmOrder(@PathVariable long id) {
        KoiFishOrder confirmedOrder = koiFishOrderService.confirmOrder(id);

        return ResponseEntity.ok(confirmedOrder);
    }
    @PreAuthorize("hasAuthority('CONSULTING')")
    @PutMapping("delivering/{id}")
    public ResponseEntity updateDeliveringOrder(@RequestBody Date deliveringDate, @PathVariable long id) {
        KoiFishOrder confirmedOrder = koiFishOrderService.deliveringOrder(deliveringDate,id);
        return ResponseEntity.ok(confirmedOrder);
    }
    @PreAuthorize("hasAuthority('CONSULTING')")
    @PutMapping("done/{id}")
    public ResponseEntity updateDoneOrder(@PathVariable long id) {
        KoiFishOrder confirmedOrder = koiFishOrderService.doneOrder(id);

        return ResponseEntity.ok(confirmedOrder);
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("cancel/{id}")
    public ResponseEntity confirmOrder(@RequestBody String notes, @PathVariable long id) {
        KoiFishOrder cancelOrder = koiFishOrderService.cancelOrder(notes,id);
        return ResponseEntity.ok(cancelOrder);
    }

}
