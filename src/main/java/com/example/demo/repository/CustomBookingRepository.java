package com.example.demo.repository;

import com.example.demo.entity.CustomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomBookingRepository extends JpaRepository<com.example.demo.entity.CustomBooking,Long> {
    CustomBooking findCustomBookingByCustomBookingId(String id);
}
