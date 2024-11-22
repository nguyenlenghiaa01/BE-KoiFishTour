package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.entity.OpenTour;
import com.example.demo.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OpenTourRepository extends JpaRepository<OpenTour,Long>, JpaSpecificationExecutor<OpenTour> {
    OpenTour findOpenTourById(long id);
    List<OpenTour> findByTourAndStatus(Tour tour, String status);
}
