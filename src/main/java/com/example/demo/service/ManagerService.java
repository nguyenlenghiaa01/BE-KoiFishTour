package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Consulting;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Sale;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ManagerRequest;
import com.example.demo.repository.ConsultingRepository;
import com.example.demo.repository.ManagerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    // xu ly nhung logic lien qua
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ModelMapper modelMapper;
    public Manager createNewManager(ManagerRequest managerRequest){
        // add student vào database bằng repository
        try {
            Manager managers = modelMapper.map(managerRequest,Manager.class);
            // xac dinh account nao tai ai student nao
            // thong qua duoc filter
            // filter luu lai duoc account yeu cau tao student nay r
            Account accountRequest=authenticationService.getCurrentAccount();
            managers.setAccount(accountRequest);
            Manager newManager = managerRepository.save(managers);
            return newManager; // trả về 1 thằng mới
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate manager code!");
        }
    }
    public List<Manager> getAllManager(){
        // lay tat ca Manager trong DB
        List<Manager> managers = managerRepository.findManagersByIsDeletedFalse();
        return managers;
    }
    public Manager updateManager(Manager manager, long id){

        Manager oldManager = managerRepository.findAccountById(id);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldManager.setPhone(manager.getPhone());
        oldManager.setEmail(manager.getEmail());
        oldManager.setFullName(manager.getFullName());
        return managerRepository.save(oldManager);
    }
    public Manager deleteManager(long id){
        Manager oldManager = managerRepository.findAccountById(id);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldManager.setDeleted(true);
        return managerRepository.save(oldManager);
    }
}
