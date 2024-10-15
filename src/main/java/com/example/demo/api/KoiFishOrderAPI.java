package com.example.demo.api;


import com.example.demo.entity.KoiFishOrder;

import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.service.KoiFishOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFishOrderAPI {

    @Autowired
    private KoiFishOrderService koiFishOrderService;

    @PreAuthorize("hasAuthority('CONSULTING')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody KoiFishOrderRequest koiFishOrderRequest) {
        KoiFishOrder koiFishOrder = koiFishOrderService.createNewOrder(koiFishOrderRequest);
            return ResponseEntity.ok(koiFishOrder);
    }

    // Get danh sách đơn hàng
    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        OrderResponse orders = koiFishOrderService.getAllOrder(page, size);
        return ResponseEntity.ok(orders);
    }

    // /api/order/{id} => id của đơn hàng mà mình muốn cập nhật
    @PreAuthorize("hasAuthority('CONSULTING')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody KoiFishOrderRequest koiFishOrderRequest, @PathVariable long id) {
            KoiFishOrder updatedOrder = koiFishOrderService.updateOrder(koiFishOrderRequest, id);
            return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasAuthority('CONSULTING')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {

            KoiFishOrder deletedOrder = koiFishOrderService.deleteOrderCart(id);
            return ResponseEntity.ok(deletedOrder);
    }

}
