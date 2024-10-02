package com.example.demo.api;


import com.example.demo.entity.OrderCart;

import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OrderAPI{
    @Autowired
    OrderService orderService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody OrderCart orderCart) {
        OrderCart newFarm = orderService.createNewOrder(orderCart);
        //return ve font end
        return ResponseEntity.ok(newFarm);
    }

    // Get danh sách order
    @GetMapping
    public ResponseEntity get(){
        List<OrderCart> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }
    // /api/order/{id} => id cua thang order minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateOrder(@Valid @RequestBody OrderCart order, @PathVariable long id){//valid kich hoat co che vadilation
        OrderCart newOrder = orderService.updateOrder(order,id);
        return ResponseEntity.ok(newOrder);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteOrder(@PathVariable long id) {
        OrderCart newOrder = orderService.deleteOrderCart(id);
        return ResponseEntity.ok(newOrder);
    }

}
