package com.example.demo.api;

import com.example.demo.entity.ShoppingCart;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoppingCart")
@CrossOrigin("*")
public class ShoppingCartAPI {

    @Autowired
    ShoppingCartService shoppingCartService;
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart newShoppingCart = shoppingCartService.createShoppingCart(shoppingCartRequest);
        return ResponseEntity.ok(newShoppingCart);
    }

    @GetMapping
    public ResponseEntity<?> get(){
        List<ShoppingCart> newShoppingCart = shoppingCartService.getAllShoppingCart();
        return ResponseEntity.ok(newShoppingCart);
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest, @PathVariable long id) {
        ShoppingCart updatedShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartRequest, id);
        return ResponseEntity.ok(updatedShoppingCart);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        ShoppingCart deletedTour = shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.ok(deletedTour);
    }
}
