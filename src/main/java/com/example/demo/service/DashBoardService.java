package com.example.demo.service;

import com.example.demo.Enum.Role;
import com.example.demo.entity.Account;
import com.example.demo.exception.AuthException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.KoiFishOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashBoardService {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    KoiFishOrderRepository koiFishOrderRepository;
    @Autowired
    BookingRepository bookingRepository;

    public Map<String, Object> getDashboardStat() {
        Map<String, Object> stats = new HashMap<>();

        long totalOrder = koiFishOrderRepository.count();
        long booking = bookingRepository.count();
        long customersCount = accountRepository.countByRole(Role.CUSTOMER);
        List<Object[]> topProducts = bookingRepository.findTop5ToursWithMostBookings();

        List<Map<String, Object>> topProductList = new ArrayList<>();
        for (Object[] productsData : topProducts) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("bookingId", productsData[0]);
            productInfo.put("tourName", productsData[1]);
            productInfo.put("totalSold", productsData[2]);
            topProductList.add(productInfo);
        }
        stats.put("Total Order", totalOrder);
        stats.put("Customer count", customersCount);
        stats.put("Booking", booking);
        stats.put("Top products", topProductList);
        return stats;

    }

//    public Map<String, Object> getMonthlyRevenue() {
//        Map<String, Object> revenueData = new HashMap<>();
//        Account account = authenticationService.getCurrentAccount();
//        if (account == null) {
//            throw new AuthException("You are not logged in");
//        }
//        List<Object[]> monthlyRevenue =
//                bookingRepository.calculateMonthlyRevenue(authenticationService.getCurrentAccount().getId());
//        List<Map<String, Object>> revenueList = new ArrayList<>();
//        for (Object[] result : monthlyRevenue) {
//            Map<String, Object> revenue = new HashMap<>();
//            revenue.put("Year", result[0]);
//            revenue.put("Month", result[1]);
//            revenue.put("Total revenue", result[2]);
//            revenueList.add(revenue);
//        }
//        revenueData.put("Monthly revenue", revenueList);
//        return revenueData;
//    }

}
