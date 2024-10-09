package com.example.demo.repository;

import com.example.demo.entity.OrderCart;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository  extends JpaRepository<OrderCart,Long> {
    OrderCart findOrderById(long id);
    List<OrderCart> findOrderCartsByIsDeletedFalse();

    Page<OrderCart> findAll(Pageable pageable);
}
