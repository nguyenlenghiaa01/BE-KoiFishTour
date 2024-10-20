package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.OpenTour;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.repository.OpenTourRepository;
import com.example.demo.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenTourService {
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OpenTourRepository openTourRepository;

    @Autowired
    private TourRepository tourRepository;


//    public OpenTour createNewOpenTour(OpenTourRequest openTourRequest) {
//        Tour tour = tourRepository.findById(openTourRequest.getTourId())
//                .orElseThrow(() -> new NotFoundException("Tour does not exist"));
//
//        OpenTour newOpenTour = modelMapper.map(openTourRequest, OpenTour.class);
//        newOpenTour.setTour(tour);
//        try {
//            return openTourRepository.save(newOpenTour);
//        } catch (Exception e) {
//            throw new DuplicateEntity("Duplicate Open Tour ID !");
//        }
//    }

    public List<OpenTour> getAllOpenTour(){
        return openTourRepository.findOpenToursByIsDeletedFalse();
    }

//    public OpenTour updateOpenTour(OpenTourRequest openTourRequest, long id){
//        OpenTour oldOpenTour = openTourRepository.findOpenTourById(id);
//        if (oldOpenTour == null) {
//            throw new NotFoundException("Open Tour not found !");
//        }
//
//        Tour tour = tourRepository.findById(openTourRequest.getTourId())
//                .orElseThrow(() -> new NotFoundException("Tour does not exist"));
//
//        modelMapper.map(openTourRequest, oldOpenTour);
//        oldOpenTour.setTour(tour);
//        return openTourRepository.save(oldOpenTour);
//    }
//    public OpenTour update(OpenTourRequest openTourRequest, long id) {
//        OpenTour openTour = openTourRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("OpenTour not exist!"));
//
//        Tour tour = tourRepository.findById(openTourRequest.getTourId())
//                .orElseThrow(() -> new NotFoundException("Tour not exist!"));
//
//        tour.setPrice(openTourRequest.getPrice());
//        tour.setStatus(openTourRequest.getStatus());
//
//        tourRepository.save(tour);
//
//        return openTour;
//    }



    public OpenTour deleteOpenTour(long id){
        OpenTour oldOpenTour = openTourRepository.findOpenTourById(id);
        if (oldOpenTour == null) {
            throw new NotFoundException("Open Tour not found !");
        }
        oldOpenTour.setDeleted(true);
        return openTourRepository.save(oldOpenTour);
    }
}

