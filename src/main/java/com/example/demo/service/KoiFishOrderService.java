package com.example.demo.service;


import com.example.demo.entity.KoiFishOrder;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.KoiFishOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KoiFishOrderService {

    @Autowired
    private KoiFishOrderRepository koiFishOrderRepository;

    @Autowired
    private KoiRepository koiRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public KoiFishOrder createNewOrder(KoiFishOrderRequest koiFishOrderRequest) {
        KoiFishOrder orders = modelMapper.map(koiFishOrderRequest, KoiFishOrder.class);

        try {
            return koiFishOrderRepository.save(orders);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntity("Duplicate Order id!");
        }

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
        KoiFishOrder oldKoiFishOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldKoiFishOrder.setTotalPrice(koiFishOrderRequest.getTotalPrice());
        oldKoiFishOrder.setQuantity(koiFishOrderRequest.getQuantity());

        return koiFishOrderRepository.save(oldKoiFishOrder);
    }
//    public OrderCart updateCart(Long orderId, Long fishId, int quantity) {
//        // Tìm đơn hàng theo orderId
//        OrderCart order = orderRepository.findOrderById(orderId);
//        if (order == null) {
//            throw new RuntimeException("Order not found");
//        }
//
//        KoiFish koiFish = koiRepository.findKoiById(fishId);
//        if (koiFish == null) {
//            throw new RuntimeException("Fish not found");
//        }
//
//        // Kiểm tra xem cá koi đã có trong giỏ hàng chưa
//        if (order.getKoiFishes().stream().anyMatch(fish -> fish.getId().contains(koiFish.getId()))) {
//            throw new DuplicateEntity("Fish already exists in the cart!");
//        } else {
//            // Thêm cá koi vào giỏ hàng
//            order.getKoiFishes().add(koiFish);
//        }
//
//        // Lưu thay đổi
//        orderRepository.save(order);
//
//        return order;
//    }



    public KoiFishOrder deleteOrderCart(long id) {
        KoiFishOrder oldKoiFishOrder = koiFishOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldKoiFishOrder.setDeleted(true);
        return koiFishOrderRepository.save(oldKoiFishOrder);
    }

}

