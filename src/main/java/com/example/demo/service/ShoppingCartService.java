package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Breed;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    AuthenticationService authenticationService;
    public ShoppingCart createShoppingCart(ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart shoppingCart = modelMapper.map(shoppingCartRequest, ShoppingCart.class);
        Account currentAccount = authenticationService.getCurrentAccount();
        if (currentAccount == null) {
            throw new RuntimeException("Customer account not found.");
        }
        shoppingCart.setCustomer(currentAccount);

        if (shoppingCartRequest.getBreedIds() != null && !shoppingCartRequest.getBreedIds().isEmpty()) {
            Set<Breed> breeds = new HashSet<>();

            for (String breedId : shoppingCartRequest.getBreedIds()) {
                Breed breed = breedRepository.findById(breedId)
                        .orElseThrow(() -> new NotFoundException("ID " + breedId + " not exist"));
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
        for (String breedId : shoppingCartRequest.getBreedIds()) {
            Breed breed = breedRepository.findBreedByBreedId(breedId);
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
