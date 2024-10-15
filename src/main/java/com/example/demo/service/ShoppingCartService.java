package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    BreedRepository breedRepository;

    @Autowired
    AuthenticationService authenticationService;
    public ShoppingCart createShoppingCart(ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart shoppingCart = new ShoppingCart();

        if (shoppingCartRequest.getBreedIds() != null && !shoppingCartRequest.getBreedIds().isEmpty()) {
            Set<Breed> breeds = new HashSet<>();

            for (Long breedId : shoppingCartRequest.getBreedIds()) {
                Breed breed = breedRepository.findById(breedId)
                        .orElseThrow(() -> new NotFoundException("Giống với ID " + breedId + " không tồn tại!"));
                breeds.add(breed);
            }
            shoppingCart.setBreeds(breeds);
        }
        shoppingCart.setStatus(shoppingCartRequest.isStatus());
        return shoppingCartRepository.save(shoppingCart);
    }


    public List<ShoppingCart> getAllShoppingCart() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findShoppingCartByIsDeletedFalse();
        return  shoppingCarts;
    }

    public ShoppingCart updateShoppingCart(ShoppingCartRequest shoppingCartRequest, long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(id);
        if (shoppingCart == null) {
            throw new NotFoundException("Shopping Cart not found!");
        }
        Set<Breed> breeds = new HashSet<>();
        for (Long breedId : shoppingCartRequest.getBreedIds()) {
            Breed breed = breedRepository.findBreedById(breedId);
            if (breed != null) {
                breeds.add(breed);
            }
        }
        shoppingCart.setBreeds(breeds);
        shoppingCart.setStatus(shoppingCartRequest.isStatus());
        if (shoppingCart.isStatus()) {
            shoppingCartRepository.delete(shoppingCart);
            return null;
        }
        return shoppingCartRepository.save(shoppingCart);
    }
    public ShoppingCart deleteShoppingCart(long id) {
        ShoppingCart oldShoppingCart = shoppingCartRepository.findShoppingCartById(id);
        if(oldShoppingCart == null) {
            throw new NotFoundException("Shopping Cart not found!");
        }
        oldShoppingCart.setDeleted(true);
        return shoppingCartRepository.save(oldShoppingCart);
    }
}
