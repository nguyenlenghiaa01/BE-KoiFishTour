package com.example.demo.api;


import com.example.demo.entity.OrderCart;

import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OrderRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            OrderCart newOrder = orderService.createNewOrder(orderRequest);
            return ResponseEntity.ok(newOrder);
        } catch (DuplicateEntity e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get danh sách đơn hàng
    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        OrderResponse orders = orderService.getAllOrder(page, size);
        return ResponseEntity.ok(orders);
    }

    // /api/order/{id} => id của đơn hàng mà mình muốn cập nhật
    @PutMapping("{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody OrderRequest orderRequest, @PathVariable long id) {
        try {
            OrderCart updatedOrder = orderService.updateOrder(orderRequest, id);
            return ResponseEntity.ok(updatedOrder);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {
        try {
            OrderCart deletedOrder = orderService.deleteOrderCart(id);
            return ResponseEntity.ok(deletedOrder);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
