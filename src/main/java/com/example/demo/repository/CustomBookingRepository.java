package com.example.demo.repository;

import com.example.demo.entity.CustomBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomBookingRepository extends JpaRepository<com.example.demo.entity.CustomBooking,Long> {
    CustomBooking findCustomBookingByCustomBookingId(String id);

    Page<CustomBooking> findAllByAccount_Code(String id, Pageable pageable);

    Page<CustomBooking> findByCustomer_Code(String id, Pageable pageable);


}
