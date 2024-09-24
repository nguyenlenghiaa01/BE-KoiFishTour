package com.example.demo.repository;

import com.example.demo.entity.SaleStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleStaffRepository extends JpaRepository<SaleStaff,Long> {
    SaleStaff findSaleStaffById(long id);
    List<SaleStaff>findSaleStaffsByIsDeletedFalse();
}
