package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BreedRequest;
import com.example.demo.repository.BreedRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Breed> getAllBreed(){
        // lay tat ca student trong DB
        List<Breed> breeds = breedRepository.findBreedsByIsDeletedFalse();
        return breeds;
    }
    public Breed updateBreed(Breed breed, long BreedId){

        Breed oldBreed = breedRepository.findBreedById(BreedId);
        if(oldBreed ==null){
            throw new NotFoundException("Breed not found !");//dung viec xu ly ngay tu day
        }
        //=> co breed co ton tai;
        oldBreed.setBreedName(breed.getBreedName());
        oldBreed.setDescription(breed.getDescription());
        return breedRepository.save(oldBreed);
    }
    public Breed deleteBreed(long BreedId){
        Breed oldBreed = breedRepository.findBreedById(BreedId);
        if(oldBreed ==null){
            throw new NotFoundException("Breed not found !");//dung viec xu ly ngay tu day
        }
        oldBreed.setDeleted(true);
        return breedRepository.save(oldBreed);
    }
}
