package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Booking findBookingById(long bookingId);
    List<Booking> findBookingsByIsDeletedFalse();

    Page<Booking> findAll(Pageable pageable);
    List<Booking>findBookingByIsDeletedTrue();

    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.bookingDate) = :month AND YEAR(b.bookingDate) = :year")
    Long countBookingsByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(b.price) FROM Booking b WHERE MONTH(b.startDate) = :month AND YEAR(b.startDate) = :year AND b.isDeleted = false")
    Long sumPriceByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.isDeleted = true AND MONTH(b.startDate) = :month AND YEAR(b.startDate) = :year")
    Long countDeletedBookingsByMonthAndYear(@Param("month") int month, @Param("year") int year);

}
