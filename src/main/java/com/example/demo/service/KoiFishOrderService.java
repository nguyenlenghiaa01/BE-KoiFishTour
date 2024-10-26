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
    BookingRepository bookingRepository;


    @Autowired
    private AuthenticationService authenticationService;
    private final ModelMapper modelMapper = new ModelMapper();

    public KoiFishOrder create(KoiFishOrderRequest koiFishOrderRequest) {
        Account customer = accountRepository.findById(koiFishOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found!"));
        Account consulting = accountRepository.findById(koiFishOrderRequest.getConsultingId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));
        Booking booking = bookingRepository.findById(koiFishOrderRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking not found!"));
        KoiFishOrder order = new KoiFishOrder();
        order.setCreateAt(new Date());
        order.setCustomer(customer);
        order.setConsulting(consulting);
        order.setBooking(booking);
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

        List<ShoppingCart> orderDetailsList = new ArrayList<>();
        for (Long koiFishId : koiFishOrderRequest.getShoppingCart()) {
            KoiFish koi = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish not found for ID: " + koiFishId));

            ShoppingCart orderDetails = new ShoppingCart();
            orderDetails.setKoiFish(koi);
            orderDetailsList.add(orderDetails);
        }

        existingOrder.setShoppingCarts(orderDetailsList);

        existingOrder.setTotalPrice(koiFishOrderRequest.getTotalPrice());
        existingOrder.setPaidMoney(koiFishOrderRequest.getPaidMoney());

        return koiFishOrderRepository.save(existingOrder);
    }

//    public List<KoiFishOrder> getKoiFishesFromOrders() {
//        Account currentAccount = authenticationService.getCurrentAccount();
//        Long customerId = currentAccount.getId(); // Lấy ID của customer hiện tại
//
//        List<KoiFishOrder> orders = koiFishOrderRepository.findByCustomerId(customerId);
//
//        if (orders.isEmpty()) {
//            System.out.println("Không tìm thấy đơn hàng cho customer ID: " + customerId);
//            return Collections.emptyList(); // Trả về danh sách rỗng nếu không có đơn hàng
//        }
//        Set<KoiFish> koiFishes = new HashSet<>();
//        for (KoiFishOrder order : orders) {
//            if (order.getShoppingCarts() != null) {
//                for (ShoppingCart cart : order.getShoppingCarts()) {
//                    if (cart.getKoiFish() != null) {
//                        koiFishes.add(cart.getKoiFish());
//                    }
//                }
//            }
//        }
//
//        return new ArrayList<>(koiFishes);
//    }

//    public List<KoiFishOrder> getKoiFishesFromOrders() {
//        Account currentAccount = authenticationService.getCurrentAccount();
//        Long customerId = currentAccount.getId();
//    }

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

}

