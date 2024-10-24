package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Request.ShoppingCartRequest;
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
    public KoiFishOrder create(KoiFishOrderRequest koiFishOrderRequest) {
        Account customer = accountRepository.findById(koiFishOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found for ID: " + koiFishOrderRequest.getCustomerId()));
         Account consulting = accountRepository.findById(koiFishOrderRequest.getConsultingId())
                 .orElseThrow(() -> new RuntimeException("Consultant not found for ID: " + koiFishOrderRequest.getConsultingId()));

        KoiFishOrder order = new KoiFishOrder();
        order.setCreateAt(new Date());
        order.setCustomer(customer);

        List<ShoppingCart> orderDetailsList = new ArrayList<>();
        double total = 0;
        for (Long koiFishId : koiFishOrderRequest.getShoppingCart()) {
            KoiFish koi = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish not found for ID: " + koiFishId));

            ShoppingCart orderDetails = new ShoppingCart();
            orderDetails.setKoiFish(koi);
            orderDetails.setKoiFishOrder(order);
            orderDetailsList.add(orderDetails);

            total += koiFishOrderRequest.getPrice();
        }

        order.setShoppingCarts(orderDetailsList);

        order.setQuantity(koiFishOrderRequest.getQuantity());
        order.setPrice(koiFishOrderRequest.getPrice());
//        float totalAmount = total+total.
        order.setTotal(total);

        return koiFishOrderRepository.save(order);
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
    public double calculateTotal(KoiFishOrder koiFishOrder) {
        double total = 0;

        Account currentAccount = authenticationService.getCurrentAccount();
        Long customerId = currentAccount.getId();
        System.out.println("Customer ID: " + customerId);
        for (ShoppingCart cart : koiFishOrder.getShoppingCarts()) {
            KoiFish koiFish = cart.getKoiFish();
            total += koiFishOrder.getPrice() * koiFishOrder.getQuantity();
        }
        koiFishOrder.setTotal(total);
        koiFishOrderRepository.save(koiFishOrder);

        return total;
    }


    public KoiFishOrder updateOrder(KoiFishOrderRequest koiFishOrderRequest, long id) {
        KoiFishOrder existingOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));
        Account customer = accountRepository.findById(koiFishOrderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found."));
        existingOrder.setCustomer(customer);
        Account consulting = accountRepository.findById(koiFishOrderRequest.getConsultingId())
                .orElseThrow(() -> new RuntimeException("Consultant not found for ID: " + koiFishOrderRequest.getConsultingId()));

        existingOrder.setConsulting(consulting);
        List<ShoppingCart> orderDetailsList = new ArrayList<>();
        double total = 0;
        for (Long koiFishId : koiFishOrderRequest.getShoppingCart()) {
            KoiFish koi = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new RuntimeException("KoiFish not found for ID: " + koiFishId));

            ShoppingCart orderDetails = new ShoppingCart();
            orderDetails.setKoiFish(koi);
            orderDetailsList.add(orderDetails);

            total += koiFishOrderRequest.getPrice();
        }

        existingOrder.setShoppingCarts(orderDetailsList);

        existingOrder.setQuantity(koiFishOrderRequest.getQuantity());
        existingOrder.setPrice(koiFishOrderRequest.getPrice());
        existingOrder.setTotal(total);

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



    public KoiFishOrder deleteOrderCart(long id) {
        KoiFishOrder oldKoiFishOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldKoiFishOrder.setDeleted(true);
        return koiFishOrderRepository.save(oldKoiFishOrder);
    }

}

