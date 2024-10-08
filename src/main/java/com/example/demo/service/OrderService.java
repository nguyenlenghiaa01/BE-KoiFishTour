package com.example.demo.service;


import com.example.demo.entity.OrderCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OrderRequest;
import com.example.demo.model.Response.OrderResponse;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    // xu ly nhung logic lien qua
    @Autowired
    OrderRepository orderRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    KoiRepository koiRepository;
    public OrderCart createNewOrder(OrderRequest orderRequest){
        OrderCart orders = modelMapper.map(orderRequest,OrderCart.class);
        try {
            OrderCart newOrder = orderRepository.save(orders);
            return newOrder;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Order id !");
        }

    }
    public OrderResponse getAllOrder(int page, int size){
        Page orderPage = orderRepository.findAll(PageRequest.of(page, size));
        List<OrderCart> orderCarts = orderPage.getContent();
        List<OrderCart> activeOrderCarts = new ArrayList<>();

        for (OrderCart order : orderCarts) {
            if(!order.isDeleted()) {
                activeOrderCarts.add(order);
            }
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setListOrderCart(activeOrderCarts);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPages(orderPage.getTotalPages());

        return  orderResponse;
    }
    public OrderCart updateOrder(OrderRequest order, long id){
        // buoc 1: tim toi thang order co id nhu la FE cung cap
        OrderCart oldOrderCart = orderRepository.findOrderById(id);
        if(oldOrderCart ==null){
            throw new NotFoundException("Order not found !");//dung viec xu ly ngay tu day
        }
        //=> co order co ton tai;
        oldOrderCart.setTotalPrice(order.getTotalPrice());
        oldOrderCart.setQuantity(order.getQuantity());
        oldOrderCart.setKoiFishes(order.getKoiFishes());
        return orderRepository.save(oldOrderCart);
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



    public OrderCart deleteOrderCart(long id){
        OrderCart oldOrderCart = orderRepository.findOrderById(id);
        if(oldOrderCart ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldOrderCart.setDeleted(true);
        return orderRepository.save(oldOrderCart);
    }
//    public OrderCart addToCart(long id){
//        OrderCart oldOrderCart = orderRepository.findOrderById(id);
//    }
}
