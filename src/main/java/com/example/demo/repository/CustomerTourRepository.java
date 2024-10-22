package com.example.demo.repository;

import com.example.demo.entity.Breed;
import com.example.demo.entity.CustomTour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTourRepository extends JpaRepository<CustomTour,Long> {
    CustomTour findCustomerTourById(long id);
    Page<CustomTour> findAll(Pageable pageable);
}