package com.example.demo.api;

import com.example.demo.Enum.Role;
import com.example.demo.model.Request.BookingTotalRequest;
import com.example.demo.model.Request.KoiFishOrderTotalRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.KoiFishOrderRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.KoiFishOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class DashBoardAPI {
    @Autowired
    BookingService bookingService;
    @Autowired
    KoiFishOrderService koiFishOrderService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    KoiFishOrderRepository koiFishOrderRepository;
    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/count-booking")
    public ResponseEntity<Long> getTotalBookings(@RequestBody @Valid BookingTotalRequest bookingTotalRequest) {
        int month = bookingTotalRequest.getMonth();
        int year = bookingTotalRequest.getYear();
        Long totalBookings = bookingService.getTotalBookingsByMonthAndYear(month, year);
        return ResponseEntity.ok(totalBookings);
    }

    @PostMapping("/count-order")
    public ResponseEntity<Double> getTotalOrders(@RequestBody @Valid KoiFishOrderTotalRequest orderTotalRequest) {
        int month = orderTotalRequest.getMonth();
        int year = orderTotalRequest.getYear();
        Double totalOrders = koiFishOrderService.countOrdersByMonthAndYear(month, year);
        return ResponseEntity.ok(totalOrders);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalOrder = koiFishOrderRepository.count();
        stats.put("totalOrders", totalOrder);

        long customerCount = accountRepository.countByRole(Role.CUSTOMER);
        stats.put("customerCount", customerCount);

        List<Object[]> topTour = bookingRepository.findTop5ToursWithMostBookings();
        List<Map<String, Object>> topProductList = new ArrayList<>();

        for (Object[] productData : topTour) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("TourName", productData[1]);
            productInfo.put("TotalBooking", productData[2]);
            topProductList.add(productInfo);
        }
        stats.put("topTour", topProductList);

        return ResponseEntity.ok(stats);
    }
}
