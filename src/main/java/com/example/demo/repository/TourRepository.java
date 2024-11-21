package com.example.demo.repository;

import com.example.demo.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Long> , JpaSpecificationExecutor<Tour> {
    Tour findTourByTourId(String tourId);
    Tour findTourById(long id);
    List<Tour> findByStatus(String status);
    List<Tour> findToursByIsDeletedFalse();

    Page<Tour> findByStatusIgnoreCase(String status, Pageable pageable);
    Page<Tour>findAll(Pageable pageable);

    List<Tour> findToursByStartDate(LocalDate startDate);

    Page<Tour> findToursByAccount_Code(String id, Pageable pageable);
}
