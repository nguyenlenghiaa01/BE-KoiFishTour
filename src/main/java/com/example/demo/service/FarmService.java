package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Farm;
import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.model.Response.BookingResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FarmResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.repository.FarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmService {
    // xu ly nhung logic lien qua
    @Autowired
    FarmRepository farmRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Farm createNewFarm(FarmRequest farmRequest) {
        //add farm vao database bang repsitory
        Farm farm = modelMapper.map(farmRequest, Farm.class);
        try {
            Farm newFarm = farmRepository.save(farm);
            return newFarm;
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate farm id !");
        }

    }

    public DataResponse<FarmResponse> getAllFarm(@RequestParam int page, @RequestParam int size) {
        Page farmPage = farmRepository.findAll(PageRequest.of(page, size));
        List<Farm> farms = farmPage.getContent();
        List<FarmResponse> farmResponses = new ArrayList<>();
        for(Farm farm: farms) {
            FarmResponse farmResponse = new FarmResponse();
            farmResponse.setId(farm.getId());
            farmResponse.setFarmId(farm.getFarmId());
            farmResponse.setDeleted(farm.isDeleted());
            farmResponse.setFarmName(farm.getFarmName());
            farmResponse.setLocation(farm.getLocation());
            farmResponse.setOwner(farm.getOwner());
            farmResponse.setImage(farm.getImage());

            farmResponses.add(farmResponse);
        }

        DataResponse<FarmResponse> dataResponse = new DataResponse<FarmResponse>();
        dataResponse.setListData(farmResponses);
        dataResponse.setTotalElements(farmPage.getTotalElements());
        dataResponse.setPageNumber(farmPage.getNumber());
        dataResponse.setPageNumber(farmPage.getNumber());
        return dataResponse;

    }

    public Farm updateFarm(FarmRequest farm, long id){

        Farm oldFarm = farmRepository.findFarmById(id);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldFarm.setOwner(farm.getOwner());
        oldFarm.setFarmName(farm.getFarmName());
        oldFarm.setLocation(farm.getLocation());
        oldFarm.setImage(farm.getImage());
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
