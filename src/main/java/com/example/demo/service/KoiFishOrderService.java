package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KoiFishOrderService {

    @Autowired
    private KoiFishOrderRepository koiFishOrderRepository;

    @Autowired
    private KoiRepository koiRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    KoiRepository getKoiRepository;

    @Autowired
    private AuthenticationService authenticationService;
    private final ModelMapper modelMapper = new ModelMapper();

    public KoiFishOrder createNewOrder(KoiFishOrderRequest koiFishOrderRequest) {
        KoiFishOrder newOrder = new KoiFishOrder();

        // Lấy tài khoản hiện tại
        Account currentAccount = authenticationService.getCurrentAccount();
        if (currentAccount == null) {
            throw new RuntimeException("Customer account not found.");
        }
        newOrder.setCustomer(currentAccount);
        newOrder.setCreateAt(new Date());

        Account consulting = accountRepository.findById(koiFishOrderRequest.getConsultingId())
                .orElseThrow(() -> new RuntimeException("Consulting account not found."));
        newOrder.setConsulting(consulting);

        List<Long> koiFishIds = koiFishOrderRequest.getKoiFishId();
        if (koiFishIds.isEmpty()) {
            throw new RuntimeException("At least one KoiFish must be selected.");
        }

        List<KoiFish> koiFishes = new ArrayList<>();
        double totalAmount = 0;

        for (Long koiFishId : koiFishIds) {
            KoiFish koiFish = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish with ID " + koiFishId + " not found."));
            double quantity = koiFishOrderRequest.getQuantity();
            if (quantity <= 0) {
                throw new RuntimeException("Quantity for KoiFish ID " + koiFishId + " must be greater than 0.");
            }

            double price = koiFishOrderRequest.getPrice();
            if (price < 0) {
                throw new RuntimeException("Price for KoiFish ID " + koiFishId + " cannot be negative.");
            }

            koiFishes.add(koiFish);
            totalAmount += price * quantity;
        }

        newOrder.setKoiFishes(koiFishes);
        newOrder.setTotal(totalAmount);
        newOrder.setQuantity(koiFishOrderRequest.getQuantity());

        return koiFishOrderRepository.save(newOrder);
    }





    public Double getTotalOrderAmountByMonthAndYear(int month, int year) {
        return koiFishOrderRepository.findTotalOrderAmountByMonthAndYear(month, year);
    }
    public Double getCalculateTotalOrderAmountForMonthAndYear(int month, int year) {
        Double totalAmount = koiFishOrderRepository.findTotalOrderAmountByMonthAndYear(month, year);
        if (totalAmount == null) {
            return 0.0;
        }
        return totalAmount * 0.10;
    }

    public Long getTotalOrdersByMonthAndYear(int month,int year){
        return koiFishOrderRepository.countOrdersByMonthAndYear(month,year);
    }

    public Long getTotalDeletedOrdersByMonthAndYear(int month,int year){
        return koiFishOrderRepository.countDeletedOrdersByMonthAndYear(month,year);
    }

    public OrderResponse getAllOrder(int page, int size){
        Page orderPage = koiFishOrderRepository.findAll(PageRequest.of(page, size));
        List<KoiFishOrder> koiFishOrders = orderPage.getContent();
        List<KoiFishOrder> activeKoiFishOrders = new ArrayList<>();

        for (KoiFishOrder order : koiFishOrders) {
            if(!order.isDeleted()) {
                activeKoiFishOrders.add(order);
            }
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setListKoiFishOrder(activeKoiFishOrders);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPages(orderPage.getTotalPages());

        return  orderResponse;
    }

    public KoiFishOrder updateOrder(KoiFishOrderRequest koiFishOrderRequest, long id) {
        KoiFishOrder existingOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));
        Account customer = accountRepository.findById(koiFishOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found."));
        existingOrder.setCustomer(customer);
        List<Long> koiFishIds = koiFishOrderRequest.getKoiFishId();
        if (koiFishIds.isEmpty()) {
            throw new RuntimeException("At least one KoiFish must be selected.");
        }

        List<KoiFish> koiFishes = new ArrayList<>();
        double totalAmount = 0;
        for (Long koiFishId : koiFishIds) {
            KoiFish koiFish = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish with ID " + koiFishId + " not found."));
            double quantity = koiFishOrderRequest.getQuantity();
            if (quantity <= 0) {
                throw new RuntimeException("Quantity for KoiFish ID " + koiFishId + " must be greater than 0.");
            }

            double price = koiFishOrderRequest.getPrice();
            if (price < 0) {
                throw new RuntimeException("Price for KoiFish ID " + koiFishId + " cannot be negative.");
            }

            koiFishes.add(koiFish);
            totalAmount += price * quantity;
        }

        existingOrder.setKoiFishes(koiFishes);
        existingOrder.setTotal(totalAmount);
        existingOrder.setQuantity(koiFishOrderRequest.getQuantity());

        return koiFishOrderRepository.save(existingOrder);
    }


    public KoiFishOrder deleteOrderCart(long id) {
        KoiFishOrder oldKoiFishOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldKoiFishOrder.setDeleted(true);
        return koiFishOrderRepository.save(oldKoiFishOrder);
    }

}

