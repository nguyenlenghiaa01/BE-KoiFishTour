package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAll(Pageable pageable);

    // Count bookings for a specific month and year with PAID status
    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.openTour.startDate) = :month AND YEAR(b.openTour.startDate) = :year AND b.status = 'PAID'")
    Long countBookingsByOpenTourStartDate(@Param("month") int month, @Param("year") int year);

    // Find all bookings by account ID where isDeleted is false
    @Query("SELECT b FROM Booking b WHERE b.account.id = :accountId AND b.isDeleted = false")
    Page<Booking> findByAccountIdAndIsDeletedFalse(@Param("accountId") Long accountId, Pageable pageable);

    // Find all bookings by account code
    Page<Booking> findAllByAccount_Code(String accountCode, Pageable pageable);

    // Find a booking by booking ID
    Booking findBookingByBookingId(String bookingId);

    // Find top 5 OpenTours with the most bookings
    @Query("SELECT t.id, t.tourName, COUNT(b.id) AS bookingCount " +
            "FROM OpenTour t JOIN t.bookings b " +
            "GROUP BY t.id, t.tourName " +
            "ORDER BY bookingCount DESC")
    List<Object[]> findTop5OpenToursWithMostBookings();

    // Find all bookings by openTour ID (instead of Tour ID)
    Page<Booking> findAllByOpenTour_Id(Long openTourId, Pageable pageable);

    List<Booking> findAllByOpenTour_Id(long openTourId);

}
