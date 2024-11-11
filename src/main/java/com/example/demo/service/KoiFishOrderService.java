package com.example.demo.service;

import com.example.demo.Enum.OrderEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KoiFishOrderService {

    @Autowired
    KoiRepository koiRepository;
    @Autowired
    KoiFishOrderRepository koiFishOrderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TourRepository tourRepository;

    public KoiFishOrder create(KoiFishOrderRequest koiFishOrderRequest) {
        Account customer = accountRepository.findById(koiFishOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        Booking booking = bookingRepository.findBookingByBookingId(koiFishOrderRequest.getBookingId());
        if(booking == null) {
            throw new NotFoundException("Booking not found!");
        }
        Tour tour = tourRepository.findById(booking.getTour().getId())
                .orElseThrow(() -> new RuntimeException("Tour not found!"));
        Account account = accountRepository.findById(tour.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Consulting account not found!"));
        KoiFishOrder order = new KoiFishOrder();
        order.setCreateAt(new Date());
        order.setCustomer(customer);
        order.setBooking(booking);
        order.setTotalPrice(0);
        order.setPaidMoney(0);
        order.setStatus(OrderEnum.PENDING);
        order.setConsulting(account);
        List<ShoppingCart> orderDetailsList = new ArrayList<>();

        for (Long koiFishId : koiFishOrderRequest.getShoppingCart()) {
            KoiFish koi = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish not found for ID: " + koiFishId));

            ShoppingCart orderDetails = new ShoppingCart();
            orderDetails.setKoiFish(koi);
            orderDetails.setKoiFishOrder(order);

            orderDetailsList.add(orderDetails);
        }

        order.setShoppingCarts(orderDetailsList);


        return koiFishOrderRepository.save(order);
    }


    public OrderResponse getAllOrder(int page, int size) {
        Page orderPage = koiFishOrderRepository.findAll(PageRequest.of(page, size));
        List<KoiFishOrder> koiFishOrders = orderPage.getContent();
        List<KoiFishOrder> activeKoiFishOrders = new ArrayList<>();

        for (KoiFishOrder order : koiFishOrders) {
            if (!order.isDeleted()) {
                activeKoiFishOrders.add(order);
            }
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setListKoiFishOrder(activeKoiFishOrders);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPages(orderPage.getTotalPages());

        return orderResponse;
    }

    public OrderResponse getCustomerOrder(long customerId, int page, int size) {
        Page orderPage = koiFishOrderRepository.findOrdersByCustomerId(customerId, PageRequest.of(page, size));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setListKoiFishOrder(orderPage.getContent());
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPages(orderPage.getTotalPages());

        return orderResponse;

    }
    public KoiFishOrder updateOrder(KoiFishOrderRequest koiFishOrderRequest, long id) {
        KoiFishOrder existingOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

            existingOrder.getShoppingCarts().clear();
        List<ShoppingCart> orderDetailsList = new ArrayList<>();
        for (Long koiFishId : koiFishOrderRequest.getShoppingCart()) {
            KoiFish koi = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new NotFoundException("KoiFish not found for ID: " + koiFishId));

            ShoppingCart orderDetails = new ShoppingCart();
                orderDetails.setKoiFish(koi);
                orderDetails.setKoiFishOrder(existingOrder);
                orderDetailsList.add(orderDetails);

        }

        existingOrder.setShoppingCarts(orderDetailsList);
        existingOrder.setStatus(koiFishOrderRequest.getStatus());
        existingOrder.setTotalPrice(koiFishOrderRequest.getTotalPrice());
        existingOrder.setPaidMoney(koiFishOrderRequest.getPaidMoney());

        return koiFishOrderRepository.save(existingOrder);
    }

    public Double getTotalOrderAmountByMonthAndYear(int month, int year) {
        return koiFishOrderRepository.findTotalOrderAmountByMonthAndYear(month, year);
    }

    public Double countOrdersByMonthAndYear(int month, int year) {
        return koiFishOrderRepository.countOrdersByMonthAndYear(month, year);
    }

    public Long countDeletedOrdersByMonthAndYear(int month, int year) {
        return koiFishOrderRepository.countDeletedOrdersByMonthAndYear(month, year);
    }


    public KoiFishOrder deleteOrderCart(long id) {
        KoiFishOrder oldKoiFishOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldKoiFishOrder.setDeleted(true);
        return koiFishOrderRepository.save(oldKoiFishOrder);
    }

    public KoiFishOrder getOrderByBookingId(String id) {
        KoiFishOrder order = koiFishOrderRepository.findByBooking_BookingId(id);

        if(order == null) {
            throw new NotFoundException("Order with this booking id not found!");
        }

        return order;
    }
}

