package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ShoppingCartRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.ShoppingCartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    private ModelMapper modelMapper = new ModelMapper();
    // xu ly nhung logic lien qua
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    KoiRepository koiRepository;
    public ShoppingCart createNewShoppingCart(ShoppingCartRequest shoppingCartRequest){
        ShoppingCart shoppingCart = modelMapper.map(shoppingCartRequest, ShoppingCart.class);
        Account account = accountRepository.findById(shoppingCartRequest.getAccountId()).
                orElseThrow(() -> new NotFoundException("Account not exist!"));
        KoiFish koiFish = koiRepository.findById(shoppingCartRequest.getKoiFishId()).
                orElseThrow(() -> new NotFoundException("Koi Fish not exist!"));

        shoppingCart.setKoiFish(koiFish);
        shoppingCart.setAccount(account);
        try {

            ShoppingCart newShoppingCart = shoppingCartRepository.save(shoppingCart);
            return newShoppingCart;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Shopping Cart id !");
        }

    }
    public List<ShoppingCart> getAllShoppingCart(){
        // lay tat ca student trong DB
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findShoppingCartsByIsDeletedFalse();
        return shoppingCarts;
    }
    public ShoppingCart updateShoppingCart(ShoppingCartRequest shoppingCart, long id){

        ShoppingCart oldShoppingCart= shoppingCartRepository.findShoppingCartById(id);
        if(oldShoppingCart ==null){
            throw new NotFoundException("Shopping Cart not found !");//dung viec xu ly ngay tu day
        }
        Account account = accountRepository.findById(shoppingCart.getAccountId()).
                orElseThrow(() -> new NotFoundException("Account not exist!"));
        KoiFish koiFish = koiRepository.findById(shoppingCart.getKoiFishId()).
                orElseThrow(() -> new NotFoundException("Koi Fish not exist!"));

        oldShoppingCart.setKoiFish(koiFish);
        oldShoppingCart.setAccount(account);
        return shoppingCartRepository.save(oldShoppingCart);
    }
    public ShoppingCart deleteShoppingCart(long id){
        ShoppingCart oldShoppingCart = shoppingCartRepository.findShoppingCartById(id);
        if(oldShoppingCart ==null){
            throw new NotFoundException("ShoppingCart not found !");//dung viec xu ly ngay tu day
        }
        oldShoppingCart.setDeleted(true);
        return shoppingCartRepository.save(oldShoppingCart);
    }

}
