package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Booking findBookingById(long bookingId);
    List<Booking> findBookingsByIsDeletedFalse();
}
