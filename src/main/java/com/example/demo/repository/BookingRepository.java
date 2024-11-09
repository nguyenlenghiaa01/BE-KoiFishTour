package com.example.demo.repository;

import com.example.demo.entity.Account;
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

    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.tour.startDate) = :month AND YEAR(b.tour.startDate) = :year AND b.status = 'PAID'")
    Long countBookingsByTourStartDate(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(b.price) FROM Booking b WHERE MONTH(b.tour.startDate) = :month AND YEAR(b.tour.startDate) = :year AND b.isDeleted = false AND b.status = 'PAID'")
    Long sumPriceByTourStartDate(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.tour.startDate) = :month AND YEAR(b.tour.startDate) = :year AND b.status = 'FAILED'")
    Long countFailedBookingsByTourStartDate(@Param("month") int month, @Param("year") int year);


    //
    @Query("SELECT b FROM Booking b WHERE b.account.id = :accountId AND b.isDeleted = false")
    Page<Booking> findByAccountIdAndIsDeletedFalse(@Param("accountId") Long accountId, Pageable pageable);


    @Query("SELECT b FROM Booking b WHERE b.tour.id = :tourId AND b.isDeleted = false")
    Page<Booking> findByTourIdAndIsDeletedFalse(@Param("tourId") Long tourId, Pageable pageable);
    Page<Booking> findByAccountCode(String id, Pageable pageable);
//    Page<Booking> findAll(long id,Pageable pageable);

    Booking findBookingByBookingId(String bookingId);


}
