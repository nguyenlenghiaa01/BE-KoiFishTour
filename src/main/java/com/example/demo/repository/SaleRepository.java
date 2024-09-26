package com.example.demo.repository;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,Long> {
    Sale findSaleById(long id);
    List<Sale> findSalesByIsDeletedFalse();
}
