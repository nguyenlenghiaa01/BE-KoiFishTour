package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.OpenTour;
import com.example.demo.entity.Tour;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenToursRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.OpenTourSearchResponse;
import com.example.demo.model.Response.OpenToursResponse;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.OpenTourRepository;
import com.example.demo.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenTourService {
    @Autowired
    OpenTourRepository openTourRepository;
    @Autowired
    TourRepository tourRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    BookingRepository bookingRepository;

    public OpenTour createNewOpenTour(OpenToursRequest openToursRequest) {
        Tour tour = tourRepository.findTourById(openToursRequest.getTourId());
        if(tour == null){
            throw new NotFoundException("Tour not found");
        }
        Account sale = authenticationService.getCurrentAccount();
        OpenTour openTour = new OpenTour();
        openTour.setStatus("OPEN");
        openTour.setDuration(tour.getDuration());
        openTour.setPrice(openToursRequest.getPrice());
        openTour.setSale(sale);
        openTour.setDescription(tour.getDescription());
        openTour.setSchedule(openToursRequest.getSchedule());
        openTour.setTime(tour.getTime());
        openTour.setTourName(tour.getTourName());
        openTour.setImage(tour.getImage());
        openTour.setStartDate(openToursRequest.getStartDate());
        openTour.setTour(tour);
        openTour.setPerAdultPrice(tour.getPerAdultPrice());
        openTour.setPerChildrenPrice(tour.getPerChildrenPrice());
        tour.setStatus("OPEN");
        tourRepository.save(tour);
        return openTourRepository.save(openTour);

    }

    public DataResponse<OpenToursResponse> getAllOpenTour(@RequestParam int page, @RequestParam int size) {
        Page openPage = openTourRepository.findAll(PageRequest.of(page, size));
        List<OpenTour> openTours = openPage.getContent();
        List<OpenToursResponse> openToursResponses = new ArrayList<>();
        for(OpenTour openTour: openTours) {
            if (openTour.getStatus().equals("OPEN")) {
                OpenToursResponse openTourResponse = new OpenToursResponse();
                openTourResponse.setId(openTour.getId());
                openTourResponse.setTourId(openTour.getTour().getTourId());
                openTourResponse.setDescription(openTour.getDescription());
                openTourResponse.setDuration(openTour.getDuration());
                openTourResponse.setPrice(openTour.getPrice());
                openTourResponse.setTime(openTour.getTime());
                openTourResponse.setImage(openTour.getImage());
                openTourResponse.setStatus(openTour.getStatus());
                openTourResponse.setSaleId(openTour.getSale().getId());
                openTourResponse.setTourName(openTour.getTourName());
                openTourResponse.setStartDate(openTour.getStartDate());
                openTourResponse.setPerAdultPrice(openTour.getPerAdultPrice());
                openTourResponse.setPerChildrenPrice(openTour.getPerChildrenPrice());
                openTourResponse.setTourId(openTourResponse.getTourId());
                openTourResponse.setSchedule(openTour.getSchedule());

                openToursResponses.add(openTourResponse);
            }

        }
            DataResponse<OpenToursResponse> dataResponse = new DataResponse<OpenToursResponse>();
            dataResponse.setListData(openToursResponses);
            dataResponse.setTotalElements(openPage.getTotalElements());
            dataResponse.setPageNumber(openPage.getNumber());
            dataResponse.setTotalPages(openPage.getTotalPages());
            return dataResponse;


    }

    public OpenTour updateOpenTour(String status,double price, long id){

        OpenTour openTour = openTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Open tour not found!"));

        openTour.setStatus(status);
        openTour.setPrice(price);
        return openTourRepository.save(openTour);
    }
    public OpenTour deleteOpenTour(long id){
        OpenTour openTour = openTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Open tour not found!"));

        openTour.setDeleted(true);
        return openTourRepository.save(openTour);
    }
    public OpenTour closeTour(long id){
        OpenTour openTour = openTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Open tour not found!"));
        Tour tour = tourRepository.findTourById(openTour.getTour().getId());
        if(tour == null){
            throw new NotFoundException("Tour not found");
        }
        openTour.setStatus("NOT OPEN");
        tour.setStatus("NOT OPEN");
        tourRepository.save(tour);
        return openTourRepository.save(openTour);
    }

    public OpenTourSearchResponse getById(long id) {
        // Fetch the OpenTour entity by ID or throw an exception if not found
        OpenTour openTour = openTourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Open tour not found!"));

        // Map the OpenTour entity to OpenToursResponse
        OpenTourSearchResponse openToursResponse = new OpenTourSearchResponse();
        openToursResponse.setId(openTour.getId());
        openToursResponse.setTourId(openTour.getTour().getTourId());
        openToursResponse.setSaleId(openTour.getSale().getId());
        openToursResponse.setTourName(openTour.getTourName());
        openToursResponse.setStartDate(openTour.getStartDate());
        openToursResponse.setDuration(openTour.getDuration());
        openToursResponse.setImage(openTour.getImage());
        openToursResponse.setStatus(openTour.getStatus());
        openToursResponse.setPrice(openTour.getPrice());
        openToursResponse.setPerAdultPrice(openTour.getPerAdultPrice());
        openToursResponse.setPerChildrenPrice(openTour.getPerChildrenPrice());
        openToursResponse.setTime(openTour.getTime());
        openToursResponse.setDescription(openTour.getDescription());
        openToursResponse.setSchedule(openTour.getSchedule());
        openToursResponse.setFarmList(openTour.getTour().getFarms());

        return openToursResponse;
    }

    public String viewSchedule(long id) {
        OpenTour openTour = openTourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Open tour not found!"));

        return openTour.getSchedule();
    }

    public OpenTour setToDone(long id) {
        OpenTour openTour = openTourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Open tour not found!"));
        Tour tour = openTour.getTour();
        tour.setStatus("NOT OPEN");
        List<Booking> bookings = openTour.getBookings();

        for (Booking booking : bookings) {
            booking.setStatus("DONE");
        }

        openTour.setBookings(bookings);
        openTour.setStatus("DONE");

        return openTourRepository.save(openTour);
    }

    public List<Booking> getBookingsByOpenTour(long id) {
        OpenTour openTour = openTourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Open tour not found!"));

        return  openTour.getBookings();
    }

}
