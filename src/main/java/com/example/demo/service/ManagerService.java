package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Manager;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    // xu ly nhung logic lien qua
    @Autowired
    ManagerRepository managerRepository;
    public Manager createNewManager(Manager manager){
        //add customer vao database bang repsitory
        try {
            Manager newManager = managerRepository.save(manager);
            return newManager;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Manager id !");
        }

    }
    public List<Manager> getAllManager(){
        // lay tat ca student trong DB
        List<Manager> managers = managerRepository.findManagersByIsDeletedFalse();
        return managers;
    }
    public Manager updateManager(Manager manager, long id){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        Manager oldManager = managerRepository.findManagerById(id);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        //=> co manager co ton tai;

        oldManager.setEmail(manager.getEmail());
        oldManager.setPhone(manager.getPhone());
        oldManager.setPassword(manager.getPassword());
        return managerRepository.save(oldManager);
    }
    public Manager deleteManager(long id){
        Manager oldManger = managerRepository.findManagerById(id);
        if(oldManger ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldManger.setDeleted(true);
        return managerRepository.save(oldManger);
    }
}
