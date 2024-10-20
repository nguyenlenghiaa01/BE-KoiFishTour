package com.example.demo.service;

import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.repository.BreedRepository;
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

@Service // danh dau day la mot lop xu ly logic
public class KoiFishService {
    // xu ly nhung logic lien qua
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    KoiRepository koiRepository;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    FarmRepository farmRepository;

    public KoiFish createNewKoi(KoiFishRequest koiFishRequest) {
        KoiFish koiFish = new KoiFish();
        koiFish.setBreed(breedRepository.findById(koiFishRequest.getBreedId())
                .orElseThrow(() -> new NotFoundException("Breed not exist")));

        koiFish.setFarm(farmRepository.findById(koiFishRequest.getFarmId())
                .orElseThrow(() -> new NotFoundException("Farm not exist")));
         koiFish.setName(koiFishRequest.getName());
         koiFish.setImage(koiFishRequest.getImage());
         koiFish.setDescription(koiFishRequest.getDescription());
        try {
            return koiRepository.save(koiFish);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate Koi id !");
        }
    }


    public DataResponse<KoiFishResponse> getAllKoi(@RequestParam int page, @RequestParam int size){
        Page fishPage = koiRepository.findAll(PageRequest.of(page, size));
        List<KoiFish> koiFishes = fishPage.getContent();
        List<KoiFishResponse> koiFishResponses = new ArrayList<>();
        for(KoiFish koiFish: koiFishes) {
            KoiFishResponse koiFishResponse = new KoiFishResponse();
            koiFishResponse.setKoiCode(koiFish.getKoiId());
            koiFishResponse.setFarm(koiFish.getFarm());
            koiFishResponse.setName(koiFish.getName());
            koiFishResponse.setDescription(koiFish.getDescription());
            koiFishResponse.setBreed(koiFish.getBreed());
            koiFishResponse.setImage(koiFish.getImage());
            koiFishResponse.setId(koiFish.getId());
            koiFishResponses.add(koiFishResponse);
        }

        DataResponse<KoiFishResponse> dataResponse = new DataResponse<KoiFishResponse>();
        dataResponse.setListData(koiFishResponses);
        dataResponse.setTotalElements(fishPage.getTotalElements());
        dataResponse.setPageNumber(fishPage.getNumber());
        dataResponse.setPageNumber(fishPage.getNumber());
        return dataResponse;
    }
    public KoiFish updateKoiFish(KoiFishRequest koi, long id){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai
        oldKoi.setFarm(farmRepository.findById(koi.getFarmId()).orElseThrow(() -> new NotFoundException("Farm not exist")));
        oldKoi.setBreed(breedRepository.findById(koi.getBreedId()).orElseThrow(() -> new NotFoundException("Breed not exist")));
        oldKoi.setName(koi.getName());
        oldKoi.setImage(koi.getImage());
        return koiRepository.save(oldKoi);
    }
    public KoiFish deleteKoi(long id){
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");//dung viec xu ly ngay tu day
        }
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }
}
