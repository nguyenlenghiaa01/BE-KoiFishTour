package com.example.demo.repository;

import com.example.demo.entity.OpenTour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenTourRepository extends JpaRepository<OpenTour,Long> {
    OpenTour findOpenTourById(long id);
    List<OpenTour>findOpenToursByIsDeletedFalse();
}
