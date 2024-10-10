package com.example.demo.repository;

import com.example.demo.entity.KoiFishOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiFishOrderRepository extends JpaRepository<KoiFishOrder,Long> {
    KoiFishOrder findOrderById(long id);
    List<KoiFishOrder> findOrderCartsByIsDeletedFalse();

    Page<KoiFishOrder> findAll(Pageable pageable);
}
