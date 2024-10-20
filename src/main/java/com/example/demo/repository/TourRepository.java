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
    Tour findTourById(long id);
    List<Tour> findByStatus(String status);
    List<Tour> findToursByIsDeletedFalse();

    Page<Tour> findAll(Pageable pageable);


}
