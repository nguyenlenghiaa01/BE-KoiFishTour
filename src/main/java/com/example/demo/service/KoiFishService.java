package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishByFarmResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class KoiFishService {

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    FarmRepository farmRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TourRepository tourRepository;
    @Autowired
    OpenTourRepository openTourRepository;

    @Autowired
    CustomBookingRepository customBookingRepository;
    @Autowired
    CustomTourRepository customTourRepository;


    public KoiFish createNewKoi(KoiFishRequest koiFishRequest) {
        KoiFish koiFish = new KoiFish();
        koiFish.setBreed(breedRepository.findById(koiFishRequest.getBreedId())
                .orElseThrow(() -> new NotFoundException("Breed not exist")));
        koiFish.setFarm(farmRepository.findById(koiFishRequest.getFarmId())
                .orElseThrow(() -> new NotFoundException("Farm not exist")));
        koiFish.setImages(koiFishRequest.getImage());
        koiFish.setDescription(koiFishRequest.getDescription());

        return koiRepository.save(koiFish);

    }

    public List<KoiFish> getKoiByFarmId(long id){
        List<KoiFish> koiFish = koiRepository.findKoiFishesByFarmId(id);

        if(koiFish ==null){
            throw  new NotFoundException("Koi fish not found");
        }
        return koiFish;
    }

    public Set<KoiFish> getKoiByTourId(String tourId) {
        Tour tour = tourRepository.findTourByTourId(tourId);
        if(tour == null) {
            throw new NotFoundException("Tour not found!");
        }
        Set<Farm> farms = tour.getFarms();
        if (farms == null || farms.isEmpty()) {
            return new HashSet<>();
        }

        Set<KoiFish> uniqueKoiFishes = new HashSet<>();

        for (Farm farm : farms) {
            List<KoiFish> fishFromFarm = farm.getKoiFishes();
            if (fishFromFarm != null) {
                uniqueKoiFishes.addAll(fishFromFarm); // Add all fish to the list
            }
        }

        return uniqueKoiFishes;
    }

    public List<KoiFishByFarmResponse> getKoiByBookingId(String bookingId) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        if(booking == null){
            throw new NotFoundException("Booking not found");
        }
        OpenTour openTour = openTourRepository.findOpenTourById(booking.getOpenTour().getId());
        if(openTour == null){
            throw new NotFoundException("openTour not found");
        }
        Tour tour = openTour.getTour();
        if(tour == null) {
            throw new NotFoundException("Tour not found!");
        }
        Set<Farm> farms = tour.getFarms();
        if (farms == null || farms.isEmpty()) {
            throw new NotFoundException("Farm of this tour not found!");
        }

        List<KoiFishByFarmResponse> responseData = new ArrayList<>();
        for(Farm farm : farms) {
            KoiFishByFarmResponse koiFishByFarmResponse = new KoiFishByFarmResponse();
            koiFishByFarmResponse.setFarmName(farm.getFarmName());
            koiFishByFarmResponse.setKoiFishList(farm.getKoiFishes());

            responseData.add(koiFishByFarmResponse);
        }
        return responseData;
    }

    public List<KoiFishByFarmResponse> getKoiByCustomBookingId(String id) {
        CustomBooking booking = customBookingRepository.findCustomBookingByCustomBookingId(id);
        if(booking == null){
            throw new NotFoundException("Custom Booking not found");
        }
        CustomTour customTour = customTourRepository.findById(booking.getCustomTour().getId()).orElseThrow(() -> new NotFoundException("Custom tour not found!"));


        Set<Farm> farms = customTour.getFarms();
        if (farms == null || farms.isEmpty()) {
            throw new NotFoundException("Farm of this tour not found!");
        }

        List<KoiFishByFarmResponse> responseData = new ArrayList<>();
        for(Farm farm : farms) {
            KoiFishByFarmResponse koiFishByFarmResponse = new KoiFishByFarmResponse();
            koiFishByFarmResponse.setFarmName(farm.getFarmName());
            koiFishByFarmResponse.setKoiFishList(farm.getKoiFishes());

            responseData.add(koiFishByFarmResponse);
        }
        return responseData;
    }

    public DataResponse<KoiFishResponse> getAllKoi(@RequestParam int page, @RequestParam int size){
        Page fishPage = koiRepository.findAll(PageRequest.of(page, size));
        List<KoiFish> koiFishes = fishPage.getContent();
        List<KoiFishResponse> koiFishResponses = new ArrayList<>();
        for(KoiFish koiFish: koiFishes) {
                KoiFishResponse koiFishResponse = new KoiFishResponse();
                koiFishResponse.setKoiCode(koiFish.getKoiId());
                koiFishResponse.setFarm(koiFish.getFarm());
                koiFishResponse.setDescription(koiFish.getDescription());
                koiFishResponse.setDeleted(koiFish.isDeleted());
                koiFishResponse.setBreed(koiFish.getBreed());
                koiFishResponse.setImage(koiFish.getImages());
                koiFishResponse.setId(koiFish.getId());
                koiFishResponses.add(koiFishResponse);

        }

        DataResponse<KoiFishResponse> dataResponse = new DataResponse<KoiFishResponse>();
        dataResponse.setListData(koiFishResponses);
        dataResponse.setTotalElements(fishPage.getTotalElements());
        dataResponse.setPageNumber(fishPage.getNumber());
        dataResponse.setTotalPages(fishPage.getTotalPages());
        return dataResponse;
    }
    public DataResponse<KoiFish> getListKoiFish(@RequestParam int page, @RequestParam int size,String id){
        Page<KoiFish> koiFish = koiRepository.findAll(PageRequest.of(page,size));
        List<KoiFish> koiFishList = koiFish.getContent();
        Booking booking = bookingRepository.findBookingByBookingId(id);

        if(booking == null) {
            throw new NotFoundException("Booking not found!");
        }
        OpenTour openTour = openTourRepository.findOpenTourById(booking.getOpenTour().getId());
        if(openTour == null){
            throw new NotFoundException("openTour not found");
        }
        Tour tour = tourRepository.findById(openTour.getTour().getId())
                .orElseThrow(() -> new NotFoundException("Tour not found!"));
        Set<Farm> farms = tour.getFarms();
        if (farms.isEmpty()) {
            throw new NotFoundException("No farms found for the selected tour");
        }
        List<KoiFish> allKoiFishes = new ArrayList<>();
        for (Farm farm : farms) {
            List<KoiFish> koiFishes = farm.getKoiFishes();
            allKoiFishes.addAll(koiFishes);
        }

        DataResponse<KoiFish> dataResponse = new DataResponse<KoiFish>();
        dataResponse.setListData(koiFishList);
        dataResponse.setTotalElements(koiFish.getTotalElements());
        dataResponse.setPageNumber(koiFish.getNumber());
        dataResponse.setTotalPages(koiFish.getTotalPages());
        return dataResponse;

    }
    public KoiFish updateKoiFish(KoiFishRequest koi, long id){
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");
        }
        oldKoi.setFarm(farmRepository.findById(koi.getFarmId()).orElseThrow(() -> new NotFoundException("Farm not exist")));
        oldKoi.setBreed(breedRepository.findById(koi.getBreedId()).orElseThrow(() -> new NotFoundException("Breed not exist")));
        oldKoi.setImages(koi.getImage());
        oldKoi.setDeleted(koi.isDeleted());
        return koiRepository.save(oldKoi);
    }
    public KoiFish deleteKoi(long id){
        KoiFish oldKoi = koiRepository.findKoiById(id);
        if(oldKoi ==null){
            throw new NotFoundException("Koi not found !");
        }
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }
}
