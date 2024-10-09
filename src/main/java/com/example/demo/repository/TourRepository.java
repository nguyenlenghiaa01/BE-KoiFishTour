package com.example.demo.repository;

import com.example.demo.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Long> {
    Tour findTourById(long id);
    List<Tour> findToursByIsDeletedFalse();

    Page<Tour> findAll(Pageable pageable);
}
