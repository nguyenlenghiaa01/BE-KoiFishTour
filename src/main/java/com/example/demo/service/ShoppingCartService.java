package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Breed;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.KoiRepository;
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
    KoiRepository koiRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    AuthenticationService authenticationService;
    public ShoppingCart createShoppingCart(ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart shoppingCart = modelMapper.map(shoppingCartRequest, ShoppingCart.class);
        Account currentAccount = authenticationService.getCurrentAccount();
        if (currentAccount == null) {
            throw new NotFoundException("Customer account not found.");
        }

        shoppingCart.setCustomer(currentAccount);

        if (shoppingCartRequest.getKoiFishId() != null && !shoppingCartRequest.getKoiFishId().isEmpty()) {
            Set<KoiFish> koiFishSet = new HashSet<>();

            for (Long koiFishId : shoppingCartRequest.getKoiFishId()) {
                KoiFish koiFish = koiRepository.findById(koiFishId)
                        .orElseThrow(() -> new NotFoundException("ID " + koiFishId + " not exist"));
                koiFishSet.add(koiFish);
            }
            shoppingCart.setKoiFishes(koiFishSet);
        }

        shoppingCart.setStatus(shoppingCartRequest.getStatus());
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
        Set<KoiFish> koiFish = new HashSet<>();
        for (Long koiFishId : shoppingCartRequest.getKoiFishId()) {
            KoiFish koiFish1 = koiRepository.findKoiById(koiFishId);
            if (koiFish1 != null) {
                koiFish.add(koiFish1);
            }
        }
        shoppingCart.setKoiFishes(koiFish);
        shoppingCart.setStatus(shoppingCartRequest.getStatus());
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
