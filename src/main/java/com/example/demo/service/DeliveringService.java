package com.example.demo.service;

import com.example.demo.entity.DeliveringStaff;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.DeliveringStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveringService {
    // xu ly nhung logic lien qua
    @Autowired
    DeliveringStaffRepository deliveringStaffRepository;
    public DeliveringStaff createNewDeliveringStaff(DeliveringStaff deliveringStaff){
        //add deli vao database bang repsitory
        try {
            DeliveringStaff newDeliveringStaff = deliveringStaffRepository.save(deliveringStaff);
            return newDeliveringStaff;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Delivering staff id !");
        }
    }
    public List<DeliveringStaff> getAllDeliveringStaff(){
        // lay tat ca student trong DB
        List<DeliveringStaff> deliveringStaffs = deliveringStaffRepository.findDeliveringStaffsByIsDeletedFalse();
        return deliveringStaffs;
    }
    public DeliveringStaff updateDeliveringStaff(DeliveringStaff deliveringStaff, String DeliveringId){
        // buoc 1: tim toi thang deli co id nhu la FE cung cap
        DeliveringStaff oldDeliveringStaff = deliveringStaffRepository.findDeliveringStaffById(DeliveringId);
        if(oldDeliveringStaff ==null){
            throw new NotFoundException("Delivering staff not found !");//dung viec xu ly ngay tu day
        }
        //=> co delivering co ton tai;
        oldDeliveringStaff.setEmail(deliveringStaff.getEmail());
        oldDeliveringStaff.setPhone(deliveringStaff.getPhone());
        oldDeliveringStaff.setPassword(deliveringStaff.getPassword());
        return deliveringStaffRepository.save(oldDeliveringStaff);
    }
    public DeliveringStaff deleteDeliveringStaff(String DeliveringId){
        DeliveringStaff oldDeliveringStaff = deliveringStaffRepository.findDeliveringStaffById(DeliveringId);
        if(oldDeliveringStaff ==null){
            throw new NotFoundException("Delivering staff not found !");//dung viec xu ly ngay tu day
        }
        oldDeliveringStaff.setDeleted(true);
        return deliveringStaffRepository.save(oldDeliveringStaff);
    }
}
