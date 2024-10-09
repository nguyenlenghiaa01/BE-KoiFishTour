package com.example.demo.service;


import com.example.demo.entity.OrderCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OrderRequest;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KoiRepository koiRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public OrderCart createNewOrder(OrderRequest orderRequest) {
        OrderCart orders = modelMapper.map(orderRequest, OrderCart.class);

        try {
            return orderRepository.save(orders);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntity("Duplicate Order id!");
        }
    }

    public List<OrderCart> getAllOrder() {
        return orderRepository.findOrderCartsByIsDeletedFalse();
    }

    public OrderCart updateOrder(OrderRequest orderRequest, long id) {
        OrderCart oldOrderCart = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldOrderCart.setTotalPrice(orderRequest.getTotalPrice());
        oldOrderCart.setQuantity(orderRequest.getQuantity());

        return orderRepository.save(oldOrderCart);
    }

    public OrderCart deleteOrderCart(long id) {
        OrderCart oldOrderCart = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!"));

        oldOrderCart.setDeleted(true);
        return orderRepository.save(oldOrderCart);
    }

}

