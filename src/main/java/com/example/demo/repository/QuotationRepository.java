package com.example.demo.repository;

import com.example.demo.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Quotation findQuotationById(long id);
    List<Quotation> findQuotationsByIsDeletedFalse();
}
