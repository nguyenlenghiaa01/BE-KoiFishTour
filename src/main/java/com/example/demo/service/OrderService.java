package com.example.demo.service;


import com.example.demo.entity.OrderCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    // xu ly nhung logic lien qua
    @Autowired
    OrderRepository orderRepository;
    public OrderCart createNewManager(OrderCart order){
        //add customer vao database bang repsitory
        try {
            OrderCart newOrder = orderRepository.save(order);
            return newOrder;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Manager id !");
        }

    }
    public List<OrderCart> getAllOrder(){
        // lay tat ca student trong DB
        List<OrderCart> orders = orderRepository.findOrdersByIdIsDeletedFalse();
        return orders;
    }
    public OrderCart updateOrder(OrderCart order, String orderId){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        OrderCart oldManager = orderRepository.findOrderById(orderId);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        //=> co manager co ton tai;
        oldManager.setTotalPrice(order.getTotalPrice());
        oldManager.setQuantity(order.getQuantity());

        return orderRepository.save(oldManager);
    }
    public OrderCart deleteOrder(String orderId){
        OrderCart oldManger = orderRepository.findOrderById(orderId);
        if(oldManger ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldManger.setDeleted(true);
        return orderRepository.save(oldManger);
    }
}
