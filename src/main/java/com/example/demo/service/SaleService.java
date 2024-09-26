package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Sale;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    SaleRepository saleRepository;
    public Sale createNewSale(Sale sale){
        //add sale vao database bang repsitory
        try {
            Sale newSale = saleRepository.save(sale);
            return newSale;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate sale id !");
        }

    }
    public List<Sale> getAllSale(){
        // lay tat ca sale trong DB
        List<Sale> sales = saleRepository.findSalesByIsDeletedFalse();
        return sales;
    }
    public Sale updateSale(Sale sale, long id){

        Sale oldSale = saleRepository.findSaleById(id);
        if(oldSale ==null){
            throw new NotFoundException("Sale not found !");//dung viec xu ly ngay tu day
        }
        //=> co breed co ton tai;
        oldSale.setEmail(sale.getEmail());
        oldSale.setPhone(sale.getPhone());
        oldSale.setPassword(sale.getPassword());
        return saleRepository.save(sale);
    }
    public Sale deleteSale(long id){
        Sale oldSale = saleRepository.findSaleById(id);
        if(oldSale ==null){
            throw new NotFoundException("Sale not found !");//dung viec xu ly ngay tu day
        }
        oldSale.setDeleted(true);
        return saleRepository.save(oldSale);
    }
}
