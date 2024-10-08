package com.example.demo.api;

import com.example.demo.entity.ShoppingCart;
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
    ShoppingCartService shoppingCartService;
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart newShoppingCart = shoppingCartService.createNewShoppingCart(shoppingCartRequest);
        return ResponseEntity.ok(newShoppingCart);
    }

    // Get danh sách breed
    @GetMapping
    public ResponseEntity get(){
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAllShoppingCart();
        return ResponseEntity.ok(shoppingCarts);
    }
    // /api/ShoppingCart/{id} => id cua thang ShoppingCart minh muon update
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity updateShoppingCart(@Valid @RequestBody ShoppingCartResponse shoppingCartRequest, @PathVariable long id){//valid kich hoat co che vadilation
        ShoppingCart newShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartRequest,id);
        return ResponseEntity.ok(newShoppingCart);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity deleteShoppingCart(@PathVariable long id){
        ShoppingCart newShoppingCart = shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.ok(newShoppingCart);
    }
}
