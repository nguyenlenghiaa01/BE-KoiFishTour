package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Manager;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    @Autowired
    ManagerRepository managerRepository;
    public Manager createNewManager(Manager manager){
        //add breed vao database bang repsitory
        try {
            Manager newManager = managerRepository.save(manager);
            return newManager;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate manager id !");
        }

    }
    public List<Manager> getAllManager(){
        // lay tat ca student trong DB
        List<Manager> managers = managerRepository.findManagersByIsDeletedFalse();
        return managers;
    }
    public Manager updateManager(Manager manager, long id){

        Manager oldManager = managerRepository.findManagerById(id);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        //=> co breed co ton tai;
        oldManager.setEmail(manager.getEmail());
        oldManager.setPassword(oldManager.getPassword());
        oldManager.setPhone(manager.getPhone());
        return managerRepository.save(oldManager);
    }
    public Manager deleteManager(long id){
        Manager oldManager = managerRepository.findManagerById(id);
        if(oldManager ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldManager.setDeleted(true);
        return managerRepository.save(oldManager);
    }
}
