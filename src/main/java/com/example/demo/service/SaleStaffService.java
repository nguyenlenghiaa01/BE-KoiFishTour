package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.SaleStaff;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SaleStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SaleStaffService {
    // xu ly nhung logic lien qua
    @Autowired
    SaleStaffRepository saleStaffRepository;
    public SaleStaff createNewSaleStaff(SaleStaff saleStaff){
        //add sale vao database bang repsitory
        try {
            SaleStaff newSaleStaff = saleStaffRepository.save(saleStaff);
            return newSaleStaff;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate SaleStaff id !");
        }

    }
    public List<SaleStaff> getAllSaleStaff(){
        // lay tat ca student trong DB
        List<SaleStaff> saleStaffs = saleStaffRepository.findSaleStaffsByIsDeletedFalse();
        return saleStaffs;
    }
    public SaleStaff updateSaleStaff(SaleStaff saleStaff, long saleId){
        // buoc 1: tim toi thang Sale co id nhu la FE cung cap
        SaleStaff oldSaleStaff = saleStaffRepository.findSaleStaffById(saleId);
        if(oldSaleStaff ==null){
            throw new NotFoundException("Sale Staff not found !");//dung viec xu ly ngay tu day
        }
        //=> co sale co ton tai;
        oldSaleStaff.setEmail(saleStaff.getEmail());
        oldSaleStaff.setPhone(saleStaff.getPhone());
        oldSaleStaff.setPassword(saleStaff.getPassword());
        return saleStaffRepository.save(oldSaleStaff);
    }
    public SaleStaff deleteSaleStaff(long saleId){
        SaleStaff oldSaleStaff = saleStaffRepository.findSaleStaffById(saleId);
        if(oldSaleStaff ==null){
            throw new NotFoundException("Sale Staff not found !");//dung viec xu ly ngay tu day
        }
        oldSaleStaff.setDeleted(true);
        return saleStaffRepository.save(oldSaleStaff);
    }
}
