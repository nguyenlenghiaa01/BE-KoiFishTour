package com.example.demo.service;

import com.example.demo.entity.Farm;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.FarmRequest;
import com.example.demo.repository.FarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmService {
    // xu ly nhung logic lien qua
    @Autowired
    FarmRepository farmRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Farm createNewFarm(FarmRequest farmRequest){
        //add farm vao database bang repsitory
        Farm farm = modelMapper.map(farmRequest, Farm.class);
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
    public Farm updateFarm(Farm farm, long id){

        Farm oldFarm = farmRepository.findFarmById(id);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldFarm.setOwner(farm.getOwner());
        oldFarm.setFarmName(farm.getFarmName());
        oldFarm.setLocation(farm.getLocation());
        return farmRepository.save(oldFarm);
    }
    public Farm deleteFarm(long id){
        Farm oldFarm = farmRepository.findFarmById(id);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");//dung viec xu ly ngay tu day
        }
        oldFarm.setDeleted(true);
        return farmRepository.save(oldFarm);
    }
}
