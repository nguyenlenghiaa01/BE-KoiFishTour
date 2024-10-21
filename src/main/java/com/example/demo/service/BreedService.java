package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BreedRequest;
import com.example.demo.model.Response.BreedResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.repository.BreedRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class BreedService {
    private ModelMapper modelMapper = new ModelMapper();
    // xu ly nhung logic lien qua
    @Autowired
    BreedRepository breedRepository;
    public Breed createNewBreed(BreedRequest breedRequest){
        Breed breed = modelMapper.map(breedRequest, Breed.class);
        try {

            Breed newBreed = breedRepository.save(breed);
            return newBreed;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate breed id !");
        }

    }
    public DataResponse<BreedResponse> getAllBreed(@RequestParam int page, @RequestParam int size){
        Page breedPage = breedRepository.findAll(PageRequest.of(page, size));
        List<Breed> breeds = breedPage.getContent();
        List<BreedResponse> breedResponses = new ArrayList<>();
        for(Breed breed: breeds) {
            BreedResponse breedResponse = new BreedResponse();
            breedResponse.setId(breed.getId());
            breedResponse.setBreedId(breed.getBreedId());
            breedResponse.setBreedName(breed.getBreedName());
            breedResponse.setDescription(breed.getDescription());
            breedResponse.setDeleted(breed.isDeleted());

            breedResponses.add(breedResponse);
        }

        DataResponse<BreedResponse> dataResponse = new DataResponse<BreedResponse>();
        dataResponse.setListData(breedResponses);
        dataResponse.setTotalElements(breedPage.getTotalElements());
        dataResponse.setPageNumber(breedPage.getNumber());
        dataResponse.setTotalPages(breedPage.getTotalPages());
        return dataResponse;
    }
    public Breed updateBreed(BreedRequest breedRequest, long BreedId){

        Breed oldBreed = breedRepository.findById(BreedId).orElseThrow(() -> new NotFoundException("Breed not found!"));

        //=> co breed co ton tai;
        oldBreed.setBreedName(breedRequest.getBreedName());
        oldBreed.setDescription(breedRequest.getDescription());
        oldBreed.setDeleted(breedRequest.isDeleted());
        return breedRepository.save(oldBreed);
    }
    public Breed deleteBreed(long BreedId){
        Breed oldBreed = breedRepository.findById(BreedId).orElseThrow(() -> new NotFoundException("Breed not found!"));

        oldBreed.setDeleted(true);
        return breedRepository.save(oldBreed);
    }
}
