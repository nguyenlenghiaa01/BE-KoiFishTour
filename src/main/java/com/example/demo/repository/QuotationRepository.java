package com.example.demo.repository;

import com.example.demo.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Quotation findQuotationById(long id);
    Quotation findQuotationByQuotationId(String id);
    List<Quotation> findQuotationsByIsDeletedFalse();

    Page<Quotation> findAll(Pageable pageable);
//    Page<Quotation> findAll(Pageable pageable);
}
