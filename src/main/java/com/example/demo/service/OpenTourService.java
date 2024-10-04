package com.example.demo.service;

import com.example.demo.entity.OpenTour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.repository.OpenTourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenTourService {
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    OpenTourRepository openTourRepository;
    public OpenTour createNewOpenTour(OpenTourRequest openTourRequest){
        //add customer vao database bang repsitory
        OpenTour openTour = modelMapper.map(openTourRequest, OpenTour.class);
        try {
            OpenTour newOpenTour = openTourRepository.save(openTour);
            return newOpenTour;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Manager id !");
        }

    }
    public List<OpenTour> getAllOpenTour(){
        // lay tat ca student trong DB
        List<OpenTour> openTours = openTourRepository.findOpenToursByIsDeletedFalse();
        return openTours;
    }
    public OpenTour updateOpenTour(OpenTourRequest openTour, long id){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        OpenTour oldOpenTour = openTourRepository.findOpenTourById(id);
        if(oldOpenTour ==null){
            throw new NotFoundException("Tour open not found !");//dung viec xu ly ngay tu day
        }
        //=> co manager co ton tai;
        oldOpenTour.setTotalPrice(openTour.getTotalPrice());
        oldOpenTour.setStatus(openTour.getStatus());

        return openTourRepository.save(oldOpenTour);
    }
    public OpenTour deleteOpenTour(long id){
        OpenTour oldOpenTour = openTourRepository.findOpenTourById(id);
        if(oldOpenTour ==null){
            throw new NotFoundException("Manager not found !");//dung viec xu ly ngay tu day
        }
        oldOpenTour.setDeleted(true);
        return openTourRepository.save(oldOpenTour);
    }
}
