package com.example.demo.repository;

import com.example.demo.entity.CustomBooking;
import com.google.api.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface CustomBookingRepository extends JpaRepository<com.example.demo.entity.CustomBooking,Long> {
    CustomBooking findCustomBookingByCustomBookingId(String id);


}
