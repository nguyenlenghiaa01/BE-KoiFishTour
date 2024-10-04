package com.example.demo.service;

import com.example.demo.entity.Farm;
import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.repository.FarmRepository;
import com.example.demo.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TourService {
    // xu ly nhung logic lien qua
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    TourRepository tourRepository;

    @Autowired
    FarmRepository farmRepository;

    public Tour createNewTour(TourRequest tourRequest){
        //add tour vao database bang repsitory
        Tour tour = modelMapper.map(tourRequest, Tour.class);

        Set<Farm> farms = new HashSet<>();

        for(Long farmId : tourRequest.getFarmId()) {
            Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new NotFoundException("Farm not exist"));
            farms.add(farm);
        }

        tour.setFarms(farms);
        try {
            Tour newTour = tourRepository.save(tour);
            return newTour;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Tour id !");
        }



    }
    public List<Tour> getAllTour(){
        // lay tat ca student trong DB
        List<Tour> tours = tourRepository.findToursByIsDeletedFalse();
        return tours;
    }
    public Tour updateTour(TourRequest tour, long TourId){
        // buoc 1: tim toi thang Tour co id nhu la FE cung cap
        Tour oldTour = tourRepository.findTourById(TourId);
        if(oldTour ==null){
            throw new NotFoundException("Tour not found !");//dung viec xu ly ngay tu day
        }
        //=> co tour co ton tai;
        Set<Farm> farms = new HashSet<>();

        for(Long farmId : tour.getFarmId()) {
            Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new NotFoundException("Farm not exist"));
            farms.add(farm);
        }
        oldTour.setFarms(farms);
        oldTour.setTourName(tour.getTourName());
        oldTour.setDuration(tour.getDuration());
        oldTour.setStartDate(tour.getStartDate());
        oldTour.setImage(tour.getImage());
        return tourRepository.save(oldTour);
    }
    public Tour deleteTour(long TourId){
        Tour oldTour = tourRepository.findTourById(TourId);
        if(oldTour ==null){
            throw new NotFoundException("Tour not found !");//dung viec xu ly ngay tu day
        }
        oldTour.setDeleted(true);
        return tourRepository.save(oldTour);
    }
}
