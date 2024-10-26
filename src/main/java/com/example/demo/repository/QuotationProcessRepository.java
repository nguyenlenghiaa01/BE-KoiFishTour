package com.example.demo.repository;

import com.example.demo.entity.KoiFish;
import com.example.demo.entity.KoiFishOrder;
import com.example.demo.entity.QuotationProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationProcessRepository extends JpaRepository<QuotationProcess,Long> {
    QuotationProcess findQuotationProcessById(long id);
    List<QuotationProcess>findQuotationProcessesByIsDeletedFalse();

    Page<QuotationProcess> findAll(Pageable pageable);
}
