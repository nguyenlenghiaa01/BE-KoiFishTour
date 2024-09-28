package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,Long> {
    Sale findSaleByUserName(String userName);
    Sale findSaleByCode(String code);
    List<Sale> findSalesByIsDeletedFalse();
    Sale findAccountById(long id);
}
