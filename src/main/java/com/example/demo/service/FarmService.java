package com.example.demo.service;

import com.example.demo.entity.Farm;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmService {
    // xu ly nhung logic lien qua
    @Autowired
    FarmRepository farmRepository;
    public Farm createNewFarm(Farm farm){
        //add farm vao database bang repsitory
        try {
            Farm newFarm = farmRepository.save(farm);
            return newFarm;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate farm id !");
        }

    }
    public List<Farm> getAllFarm(){
        // lay tat ca student trong DB
        List<Farm> farms = farmRepository.findFarmsByIsDeletedFalse();
        return farms;
    }
    public Farm updateFarm(Farm farm, String farmId){

        Farm oldFarm = farmRepository.findFarmById(farmId);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldFarm.setOwner(farm.getOwner());
        oldFarm.setFarmName(farm.getFarmName());
        oldFarm.setLocation(farm.getLocation());
        return farmRepository.save(oldFarm);
    }
    public Farm deleteFarm(String farmId){
        Farm oldFarm = farmRepository.findFarmById(farmId);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");//dung viec xu ly ngay tu day
        }
        oldFarm.setDeleted(true);
        return farmRepository.save(oldFarm);
    }
}
