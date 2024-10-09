package com.example.demo.api;

import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.model.Response.ShoppingCartResponse;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ShoppingCartAPI {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        try {
            ShoppingCart newShoppingCart = shoppingCartService.createNewShoppingCart(shoppingCartRequest);
            return ResponseEntity.ok(newShoppingCart);
        } catch (DuplicateEntity e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShoppingCart>> getAll() {
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAllShoppingCart();
        return ResponseEntity.ok(shoppingCarts);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateShoppingCart(@Valid @RequestBody ShoppingCartResponse shoppingCartRequest, @PathVariable long id) {
        try {
            ShoppingCart updatedShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartRequest, id);
            return ResponseEntity.ok(updatedShoppingCart);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteShoppingCart(@PathVariable long id) {
        try {
            ShoppingCart deletedShoppingCart = shoppingCartService.deleteShoppingCart(id);
            return ResponseEntity.ok(deletedShoppingCart);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

