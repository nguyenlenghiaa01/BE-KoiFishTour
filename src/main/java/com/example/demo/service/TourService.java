package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FarmResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.repository.FarmRepository;
import com.example.demo.repository.SearchHistoryRepository;
import com.example.demo.repository.TourRepository;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TourService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    TourRepository tourRepository;

    @Autowired
    FarmRepository farmRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    public Tour createNewTour(TourRequest tourRequest) {
        Tour newTour = new Tour();
        newTour.setTourName(tourRequest.getTourName());
        newTour.setStartDate(tourRequest.getStartDate());
        newTour.setDuration(tourRequest.getDuration());
        newTour.setPrice(tourRequest.getPrice());
        newTour.setStatus("Not open");
        newTour.setTime(tourRequest.getTime());
        newTour.setImage(tourRequest.getImage());

        Set<Farm> farms = new HashSet<>();

        for (Long farmId : tourRequest.getFarmId()) {
            Farm farm = farmRepository.findById(farmId)
                    .orElseThrow(() -> new NotFoundException("Farm not exist ID: " + farmId));
            farms.add(farm);
        }
        newTour.setFarms(farms);

        try {
            return tourRepository.save(newTour);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Duplicate: " + e.getMessage());
        } catch (Exception e) {
            throw new DuplicateEntity("Tour exist ID: " + newTour.getId());
        }
    }


    public DataResponse<TourResponse> getAllTour(@RequestParam int page, @RequestParam int size) {
        Page tourPage = tourRepository.findAll(PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();
        List<TourResponse> tourResponses = new ArrayList<>();

        for (Tour tour : tours) {
                TourResponse tourResponse = new TourResponse();
                tourResponse.setId(tour.getId());
                tourResponse.setTourId(tour.getTourId());
                tourResponse.setDeleted(tour.isDeleted());
                tourResponse.setTourName(tour.getTourName());
                tourResponse.setStartDate(tour.getStartDate());
                tourResponse.setDuration(tour.getDuration());
                tourResponse.setImage(tour.getImage());
                tourResponse.setStatus(tour.getStatus());
                tourResponse.setFarms(tour.getFarms());

                tourResponses.add(tourResponse);

        }

        DataResponse<TourResponse> dataResponse = new DataResponse<TourResponse>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourPage.getTotalElements());
        dataResponse.setPageNumber(tourPage.getNumber());
        dataResponse.setTotalPages(tourPage.getTotalPages());
        return dataResponse;
    }


    public DataResponse<TourResponse> searchTours(LocalDate startDate, String duration, String farms, int page, int size) {
        Account currentAccount = authenticationService.getCurrentAccount();

        HistoryTourSearch searchHistory = new HistoryTourSearch();
        searchHistory.setStartDate(startDate);
        searchHistory.setDuration(duration);
        searchHistory.setFarm(farms);
        searchHistory.setSearchTime(LocalDateTime.now());
        searchHistory.setAccount(currentAccount);
        searchHistoryRepository.save(searchHistory);

        Set<String> farmSet = new HashSet<>();
        if (farms != null && !farms.isEmpty()) {
            String[] farmArray = farms.split(",");
            for (String farm : farmArray) {
                farmSet.add(farm.trim());
            }
        }
        Specification<Tour> specification = Specification.where(null);

        boolean hasCriteria = false;

        if (startDate != null) {
            specification = specification.or(TourSpecification.hasStartDate(startDate));
            hasCriteria = true;
        }
        if (duration != null && !duration.isEmpty()) {
            specification = specification.or(TourSpecification.hasDuration(duration));
            hasCriteria = true;
        }
        if (!farmSet.isEmpty()) {
            specification = specification.or(TourSpecification.hasFarms(farmSet));
            hasCriteria = true;
        }
        specification = specification.and(TourSpecification.hasStatus("open"));

        Page<Tour> tourPage;
        if (hasCriteria) {
            // Nếu có thông tin tìm kiếm, chỉ tìm các tour theo điều kiện
            tourPage = tourRepository.findAll(specification, PageRequest.of(page, size));
        } else {
            tourPage = tourRepository.findAll(PageRequest.of(page, size));
        }

        List<TourResponse> tourResponses = new ArrayList<>();

        for (Tour tour : tourPage.getContent()) {
            TourResponse tourResponse = new TourResponse();
            tourResponse.setId(tour.getId());
            tourResponse.setTourId(tour.getTourId());
            tourResponse.setDeleted(tour.isDeleted());
            tourResponse.setTourName(tour.getTourName());
            tourResponse.setStartDate(tour.getStartDate());
            tourResponse.setDuration(tour.getDuration());
            tourResponse.setImage(tour.getImage());
            tourResponse.setStatus(tour.getStatus());
            tourResponse.setFarms(tour.getFarms());
            tourResponse.setPrice(tour.getPrice());

            tourResponses.add(tourResponse);
        }

        DataResponse<TourResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourPage.getTotalElements());
        dataResponse.setPageNumber(tourPage.getNumber());
        dataResponse.setTotalPages(tourPage.getTotalPages());
        return dataResponse;
    }

    public DataResponse<TourResponse> getAllTourPrice(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String time) {

        // Lấy trang các tour
        Page<Tour> tourPage = tourRepository.findAll(PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();
        List<TourResponse> tourResponses = new ArrayList<>();

        for (Tour tour : tours) {
            boolean matchesCriteria = true;

            if (minPrice != null && tour.getPrice() > minPrice) {
                matchesCriteria = false;
            }
            if (maxPrice != null && tour.getPrice() < maxPrice) {
                matchesCriteria = false;
            }
            if (!"open".equals(tour.getStatus())) {
                matchesCriteria = false;
            }
            if (matchesCriteria) {
                TourResponse tourResponse = new TourResponse();
                tourResponse.setId(tour.getId());
                tourResponse.setTourId(tour.getTourId());
                tourResponse.setDeleted(tour.isDeleted());
                tourResponse.setTourName(tour.getTourName());
                tourResponse.setStartDate(tour.getStartDate());
                tourResponse.setDuration(tour.getDuration());
                tourResponse.setImage(tour.getImage());
                tourResponse.setStatus(tour.getStatus());
                tourResponse.setFarms(tour.getFarms());
                tourResponse.setPrice(tour.getPrice());
                tourResponse.setTime(tour.getTime());

                tourResponses.add(tourResponse);
            }
        }

        int totalFilteredElements = tourResponses.size();
        int totalPages = (int) Math.ceil((double) totalFilteredElements / size);

        DataResponse<TourResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(totalFilteredElements);
        dataResponse.setPageNumber(page);
        dataResponse.setTotalPages(totalPages);
        return dataResponse;
    }



    private boolean isTimeInRange(LocalTime time) {
        LocalTime morningStart = LocalTime.of(6, 0); // 6:00 AM
        LocalTime morningEnd = LocalTime.of(12, 0); // 12:00 PM
        LocalTime afternoonStart = LocalTime.of(15, 0); // 3:00 PM
        LocalTime afternoonEnd = LocalTime.of(22, 0); // 10:00 PM

        return (time.isAfter(morningStart) && time.isBefore(morningEnd)) ||
                (time.isAfter(afternoonStart) && time.isBefore(afternoonEnd));
    }




    public Tour updateTour(TourRequest tour, long TourId) {
        Tour oldTour = tourRepository.findTourById(TourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");
        }
        Set<Farm> farms = new HashSet<>();
        for (Long farmId : tour.getFarmId()) {
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

    public Tour opentour(OpenTourRequest openTourRequest, long tourId) {
        Tour oldTour = tourRepository.findTourById(tourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");
        }
        oldTour.setStatus(openTourRequest.getStatus());
        oldTour.setPrice(openTourRequest.getPrice());
        return tourRepository.save(oldTour);
    }


    public Tour deleteTour(long TourId) {
        Tour oldTour = tourRepository.findTourById(TourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");//dung viec xu ly ngay tu day
        }
        oldTour.setDeleted(true);
        return tourRepository.save(oldTour);
    }
}
