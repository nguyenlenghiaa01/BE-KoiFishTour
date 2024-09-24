package com.example.demo.service;

import com.example.demo.entity.ConsultingStaff;
import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ConsultingStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultingStaffService {
    // xu ly nhung logic lien qua
    @Autowired
    ConsultingStaffRepository consultingStaffRepository;
    public ConsultingStaff createNewConsulting(ConsultingStaff consultingStaff){
        //add ConsultingStaff vao database bang repository
        try {
            ConsultingStaff newConsultingStaff = consultingStaffRepository.save(consultingStaff);
            return newConsultingStaff;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate ConsultingStaff id !");
        }

    }
    public List<ConsultingStaff> getAllConsulting(){
        // lay tat ca student trong DB
        List<ConsultingStaff> consultingStaffs = consultingStaffRepository.findConsultingStaffsByIsDeletedFalse();
        return consultingStaffs;
    }
    public ConsultingStaff updateConsultingStaff(ConsultingStaff customer, String consultingId){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        ConsultingStaff oldConsultingStaff = consultingStaffRepository.findConsultingStaffById(consultingId);
        if(oldConsultingStaff ==null){
            throw new NotFoundException("Consulting staff not found !");//dung viec xu ly ngay tu day
        }
        //=> co Consulting staff co ton tai;
        oldConsultingStaff.setEmail(customer.getEmail());
        oldConsultingStaff.setPhone(customer.getPhone());
        oldConsultingStaff.setPassword(customer.getPassword());
        return consultingStaffRepository.save(oldConsultingStaff);
    }
    public ConsultingStaff deleteConsultingStaff(String consultingId){
        ConsultingStaff oldConsultingStaff = consultingStaffRepository.findConsultingStaffById(consultingId);
        if(oldConsultingStaff ==null){
            throw new NotFoundException("Consulting not found !");//dung viec xu ly ngay tu day
        }
        oldConsultingStaff.setDeleted(true);
        return consultingStaffRepository.save(oldConsultingStaff);
    }
}
