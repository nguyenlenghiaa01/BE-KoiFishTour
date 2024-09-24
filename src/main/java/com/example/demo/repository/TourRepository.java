package com.example.demo.repository;

import com.example.demo.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour,String> {
    Tour findTourById(String TourId);
    List<Tour> findToursByIsDeletedFalse();
}
