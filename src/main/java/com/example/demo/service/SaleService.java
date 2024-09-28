package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Sale;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.SaleRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.SaleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    // xu ly nhung logic lien qua
    @Autowired
    SaleRepository saleRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;
    public Sale createNewSale(SaleRequest saleRequest){
        // add student vào database bằng repository
        try {
            Sale sales = modelMapper.map(saleRequest,Sale.class);
            // xac dinh account nao tai ai student nao
            // thong qua duoc filter
            // filter luu lai duoc account yeu cau tao student nay r
            Account accountRequest=authenticationService.getCurrentAccount();
            sales.setAccount(accountRequest);
            Sale newSale = saleRepository.save(sales);
            return newSale; // trả về 1 thằng mới
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate sale code!");
        }

    }
    public List<Sale> getAllSale(){
        // lay tat ca Manager trong DB
        List<Sale> sales = saleRepository.findSalesByIsDeletedFalse();
        return sales;
    }
    public Sale updateSale(Sale sale, long id){
        Sale oldSale = saleRepository.findAccountById(id);
        if(oldSale ==null){
            throw new NotFoundException("Sale not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldSale.setPhone(sale.getPhone());
        oldSale.setEmail(sale.getEmail());
        oldSale.setFullName(sale.getFullName());
        return saleRepository.save(oldSale);
    }
    public Sale deleteSale(long id){
        Sale oldSale = saleRepository.findAccountById(id);
        if(oldSale ==null){
            throw new NotFoundException("Sale not found !");//dung viec xu ly ngay tu day
        }
        oldSale.setDeleted(true);
        return saleRepository.save(oldSale);
    }
}
