package com.example.demo.repository;

import com.example.demo.entity.CustomTour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomTourRepository extends JpaRepository<CustomTour,Long> {
}
