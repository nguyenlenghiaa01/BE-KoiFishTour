package com.example.demo.service;

import com.example.demo.entity.Farm;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.ShoppingCart;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FarmResponse;
import com.example.demo.repository.FarmRepository;
import com.example.demo.repository.KoiRepository;
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
    @Autowired
    KoiRepository koiRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Farm createNewFarm(FarmRequest farmRequest) {
        Farm farm = new Farm();
        farm.setFarmName(farmRequest.getFarmName());
        farm.setOwner(farmRequest.getOwner());
        farm.setImage(farmRequest.getImage());
        farm.setImage1(farmRequest.getImage1());
        farm.setImage2(farmRequest.getImage2());
        farm.setLocation(farmRequest.getLocation());
        farm.setDescription(farmRequest.getDescription());
        List<KoiFish> koi = new ArrayList<>();
        for (Long koiFishId : farmRequest.getKoi()) {
            KoiFish koiFish = koiRepository.findById(koiFishId)
                    .orElseThrow(() -> new NotFoundException("KoiFish not found for ID: " + koiFishId));
            koi.add(koiFish);
        }

        farm.setKoiFishes(koi);
        return farmRepository.save(farm);

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
            farmResponse.setImage1(farm.getImage1());
            farmResponse.setImage2(farm.getImage2());
            farmResponse.setDescription(farm.getDescription());

            farmResponses.add(farmResponse);
        }

        DataResponse<FarmResponse> dataResponse = new DataResponse<FarmResponse>();
        dataResponse.setListData(farmResponses);
        dataResponse.setTotalElements(farmPage.getTotalElements());
        dataResponse.setPageNumber(farmPage.getNumber());
        dataResponse.setTotalPages(farmPage.getTotalPages());
        return dataResponse;

    }
    public Farm getFarmById(long id){
        Farm farm = farmRepository.findFarmById(id);
        if(farm ==null){
            throw  new NotFoundException("Farm not found");
        }
        return farm;
    }

    public Farm updateFarm(FarmRequest farm, long id){

        Farm oldFarm = farmRepository.findFarmById(id);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");
        }
        oldFarm.setOwner(farm.getOwner());
        oldFarm.setFarmName(farm.getFarmName());
        oldFarm.setLocation(farm.getLocation());
        oldFarm.setImage(farm.getImage());
        oldFarm.setDeleted(farm.isDeleted());
        oldFarm.setImage1(farm.getImage1());
        oldFarm.setImage2(farm.getImage2());
        oldFarm.setDescription(farm.getDescription());
        return farmRepository.save(oldFarm);
    }
    public Farm deleteFarm(long id){
        Farm oldFarm = farmRepository.findFarmById(id);
        if(oldFarm ==null){
            throw new NotFoundException("Farm not found !");
        }
        oldFarm.setDeleted(true);
        return farmRepository.save(oldFarm);
    }
}
