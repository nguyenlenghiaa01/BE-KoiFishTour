package com.example.demo.repository;

import com.example.demo.entity.KoiFishOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KoiFishOrderRepository extends JpaRepository<KoiFishOrder,Long> {
    KoiFishOrder findOrderById(long id);
    List<KoiFishOrder> findOrderCartsByIsDeletedFalse();

    @Query("SELECT SUM(k.totalPrice) FROM KoiFishOrder k WHERE k.isDeleted = false AND MONTH(k.createAt) = :month AND YEAR(k.createAt) = :year")
    Double findTotalOrderAmountByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(k) FROM KoiFishOrder k WHERE k.isDeleted = false AND MONTH(k.createAt) = :month AND YEAR(k.createAt) = :year")
    Double countOrdersByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(k) FROM KoiFishOrder k WHERE k.isDeleted = true AND MONTH(k.createAt) = :month AND YEAR(k.createAt) = :year")
    Long countDeletedOrdersByMonthAndYear(@Param("month") int month, @Param("year") int year);

    Page<KoiFishOrder> findAll(Pageable pageable);

    Page<KoiFishOrder> findOrdersByCustomerId(long customerId, Pageable pageable);

    KoiFishOrder findByBooking_BookingId(String id);
}
