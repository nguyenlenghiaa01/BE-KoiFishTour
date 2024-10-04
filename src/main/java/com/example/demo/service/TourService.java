package com.example.demo.service;

import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {
    // xu ly nhung logic lien qua
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    TourRepository tourRepository;
    public Tour createNewTour(TourRequest tourRequest){
        //add tour vao database bang repsitory
        Tour tours =modelMapper.map(tourRequest,Tour.class);
        try {
            Tour newTour = tourRepository.save(tours);
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
