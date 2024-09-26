package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Consulting;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.ConsultingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultingService {
    @Autowired
    ConsultingRepository consultingRepository;
    public Consulting createNewConsulting(Consulting consulting){
        //add consulting vao database bang repsitory
        try {
            Consulting newConsulting = consultingRepository.save(consulting);
            return newConsulting;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate consulting id !");
        }

    }
    public List<Consulting> getAllConsulting(){
        // lay tat ca student trong DB
        List<Consulting> consultings = consultingRepository.findConsultingsByIsDeletedFalse();
        return consultings;
    }
    public Consulting updateConsulting(Consulting consulting, long id){

        Consulting oldConsulting = consultingRepository.findConsultingById(id);
        if(oldConsulting ==null){
            throw new NotFoundException("Consulting not found !");//dung viec xu ly ngay tu day
        }
        //=> co breed co ton tai;
        oldConsulting.setEmail(consulting.getEmail());
        oldConsulting.setPassword(consulting.getPassword());
        oldConsulting.setPhone(consulting.getPhone());
        return consultingRepository.save(oldConsulting);
    }
    public Consulting deleteConsulting(long id){
        Consulting oldConsulting = consultingRepository.findConsultingById(id);
        if(oldConsulting ==null){
            throw new NotFoundException("Consulting not found !");//dung viec xu ly ngay tu day
        }
        oldConsulting.setDeleted(true);
        return consultingRepository.save(oldConsulting);
    }
}
