package com.example.demo.service;


import com.example.demo.entity.OrderCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    // xu ly nhung logic lien qua
    @Autowired
    OrderRepository orderRepository;
    public OrderCart createNewOrder(OrderCart order){
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
        List<OrderCart> orders = orderRepository.findOrderCartsByIsDeletedFalse();
        return orders;
    }
    public OrderCart updateOrder(OrderCart order, long id){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        OrderCart oldOrderCart = orderRepository.findOrderById(id);
        if(oldOrderCart ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        //=> co manager co ton tai;
        oldOrderCart.setTotalPrice(order.getTotalPrice());
        oldOrderCart.setQuantity(order.getQuantity());

        return orderRepository.save(oldOrderCart);
    }
    public OrderCart deleteOrder(long id){
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
