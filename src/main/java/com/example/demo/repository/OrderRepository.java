package com.example.demo.repository;

import com.example.demo.entity.OrderCart;
import com.example.demo.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<OrderCart,String> {
    OrderCart findOrderById(String orderId);
    List<OrderCart>findOrdersByIdIsDeletedFalse();
}
