package com.example.demo.service;

import com.example.demo.entity.Farm;
import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.repository.FarmRepository;
import com.example.demo.repository.TourRepository;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Tour createNewTour(TourRequest tourRequest) {
        // Tạo đối tượng Tour mới
        Tour newTour = new Tour();
        newTour.setTourName(tourRequest.getTourName());
        newTour.setStartDate(tourRequest.getStartDate());
        newTour.setDuration(tourRequest.getDuration());
        newTour.setImage(tourRequest.getImage());

        Set<Farm> farms = new HashSet<>();

        // Lấy danh sách farm
        for (Long farmId : tourRequest.getFarmId()) {
            Farm farm = farmRepository.findById(farmId)
                    .orElseThrow(() -> new NotFoundException("Farm không tồn tại với ID: " + farmId));
            farms.add(farm);
        }
        newTour.setFarms(farms);

        try {
            return tourRepository.save(newTour);
        } catch (ConstraintViolationException e) {
            // Xử lý lỗi vi phạm ràng buộc
            throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            throw new DuplicateEntity("Tour đã tồn tại với ID: " + newTour.getId());
        }
    }


    public TourResponse getAllTour(int page, int size) {
        Page tourPage = tourRepository.findAll(PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();

        TourResponse tourResponse = new TourResponse();

        tourResponse.setListTour(tours);
        tourResponse.setPageNumber(tourPage.getNumber());
        tourResponse.setTotalElements(tourPage.getTotalElements());
        tourResponse.setTotalPages(tourPage.getTotalPages());

        return tourResponse;
    }

    public Tour updateTour(TourRequest tour, long TourId) {
        // buoc 1: tim toi thang Tour co id nhu la FE cung cap
        Tour oldTour = tourRepository.findTourById(TourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");//dung viec xu ly ngay tu day
        }
        //=> co tour co ton tai;
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

    public Tour deleteTour(long TourId) {
        Tour oldTour = tourRepository.findTourById(TourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");//dung viec xu ly ngay tu day
        }
        oldTour.setDeleted(true);
        return tourRepository.save(oldTour);
    }
}
