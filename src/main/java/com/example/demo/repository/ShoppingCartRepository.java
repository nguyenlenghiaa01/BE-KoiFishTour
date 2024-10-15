package com.example.demo.repository;

import com.example.demo.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findShoppingCartById(long id);
    List<ShoppingCart> findShoppingCartByIsDeletedFalse();

}
