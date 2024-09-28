package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Consulting;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Sale;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ConsultingRequest;
import com.example.demo.repository.ConsultingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultingService {
    // xu ly nhung logic lien qua
    @Autowired
    ConsultingRepository consultingRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ModelMapper modelMapper;
    public Consulting createNewConsulting(ConsultingRequest consultingRequest){
        // add student vào database bằng repository
        try {
            Consulting consultings = modelMapper.map(consultingRequest, Consulting.class);
            // xac dinh account nao tai ai student nao
            // thong qua duoc filter
            // filter luu lai duoc account yeu cau tao student nay r
            Account accountRequest=authenticationService.getCurrentAccount();
            consultings.setAccount(accountRequest);
            Consulting newConsulting = consultingRepository.save(consultings);
            return newConsulting; // trả về 1 thằng mới
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate consulting code!");
        }
    }
    public List<Consulting> getAllConsulting(){
        // lay tat ca student trong DB
        List<Consulting> consultings = consultingRepository.findConsultingByIsDeletedFalse();
        return consultings;
    }
    public Consulting updateConsulting(Consulting consulting, long id){

        Consulting oldConsulting = consultingRepository.findAccountById(id);
        if(oldConsulting ==null){
            throw new NotFoundException("Consulting not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldConsulting.setPhone(consulting.getPhone());
        oldConsulting.setEmail(consulting.getEmail());
        oldConsulting.setFullName(consulting.getFullName());
        return consultingRepository.save(oldConsulting);
    }
    public Consulting deleteConsulting(long id){
        Consulting oldConsulting = consultingRepository.findAccountById(id);
        if(oldConsulting ==null){
            throw new NotFoundException("Consulting not found !");//dung viec xu ly ngay tu day
        }
        oldConsulting.setDeleted(true);
        return consultingRepository.save(oldConsulting);
    }
}
