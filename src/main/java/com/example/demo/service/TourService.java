package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.model.Response.TourResponses;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FarmRepository;
import com.example.demo.repository.SearchHistoryRepository;
import com.example.demo.repository.TourRepository;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TourService {
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    TourRepository tourRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    ScheduleJob scheduleJob;
    @Autowired
    AccountRepository accountRepository;



    public Tour createNewTour(TourRequest tourRequest) {
        Account consultingAccount = accountRepository.findById(tourRequest.getConsultingId())
                .orElseThrow(() -> new NotFoundException("Account not found ID: " + tourRequest.getConsultingId()));

        Tour newTour = new Tour();
        newTour.setTourName(tourRequest.getTourName());
        newTour.setStartDate(tourRequest.getStartDate());
        newTour.setDuration(tourRequest.getDuration());
        newTour.setPrice(tourRequest.getPrice());
        newTour.setStatus("NOT OPEN");
        newTour.setTime(tourRequest.getTime());
        newTour.setDescription(tourRequest.getDescription());
        newTour.setImage(tourRequest.getImage());
        newTour.setAccount(consultingAccount);
        newTour.setPerAdultPrice(tourRequest.getPerAdultPrice());
        newTour.setPerChildrenPrice(tourRequest.getPerChildrenPrice());

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
            throw new DuplicateEntity("Tour exists ID: " + newTour.getId());
        }
    }


    public Tour getTourId(String id){
        Tour tour1 = tourRepository.findTourByTourId(id);
        return tour1;
    }



    public DataResponse<TourResponse> getAllTour(@RequestParam int page, @RequestParam int size) {
        Page<Tour> tourPage = tourRepository.findByStatusIgnoreCase("OPEN", PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();
        List<TourResponse> tourResponses = new ArrayList<>();

        for (Tour tour : tours) {
            if(!tour.isDeleted()) {
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
                tourResponse.setPerAdultPrice(tour.getPerAdultPrice());
                tourResponse.setPerChildrenPrice(tour.getPerChildrenPrice());
                tourResponse.setDescription(tour.getDescription());
                if (tour.getAccount() != null) {
                    tourResponse.setConsultingId(tour.getAccount().getId());
                }
                tourResponses.add(tourResponse);
            }
            }


        DataResponse<TourResponse> dataResponse = new DataResponse<TourResponse>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourPage.getTotalElements());
        dataResponse.setPageNumber(tourPage.getNumber());
        dataResponse.setTotalPages(tourPage.getTotalPages());
        return dataResponse;
    }

    public DataResponse<TourResponse> getAllTourNotOpen(@RequestParam int page, @RequestParam int size) {
        Page<Tour> tourPage = tourRepository.findByStatusIgnoreCase("NOT OPEN", PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();
        List<TourResponse> tourResponses = new ArrayList<>();

        for (Tour tour : tours) {
            if(!tour.isDeleted()) {
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
                tourResponse.setPerAdultPrice(tour.getPerAdultPrice());
                tourResponse.setPerChildrenPrice(tour.getPerChildrenPrice());
                tourResponse.setTime(tour.getTime());
                tourResponse.setDescription(tour.getDescription());
                if (tour.getAccount() != null) {
                    tourResponse.setConsultingId(tour.getAccount().getId());
                }

                tourResponses.add(tourResponse);
            }
            }


        DataResponse<TourResponse> dataResponse = new DataResponse<TourResponse>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourPage.getTotalElements());
        dataResponse.setPageNumber(tourPage.getNumber());
        dataResponse.setTotalPages(tourPage.getTotalPages());
        return dataResponse;
    }

    public DataResponse<TourResponse> searchTours(int page, int size, LocalDate startDate, BigDecimal min, BigDecimal max, Set<String> farms) {
        // Check if all the input parameters are null or empty
        if (startDate == null && min == null && max == null && (farms == null || farms.isEmpty())) {
            // Return empty response if all inputs are null or empty
            DataResponse<TourResponse> emptyResponse = new DataResponse<>();
            emptyResponse.setListData(new ArrayList<>());  // Empty list
            emptyResponse.setTotalElements(0);  // No elements
            emptyResponse.setPageNumber(page);  // Current page
            emptyResponse.setTotalPages(0);  // No pages
            return emptyResponse;
        }

        // Set of farm names
        Set<String> farmSet = new HashSet<>();
        if (farms != null && !farms.isEmpty()) {
            farmSet.addAll(farms);
        }

        // Specification to filter based on different criteria
        Specification<Tour> specification = Specification.where(TourSpecification.hasStatus("OPEN"));

        if (startDate != null) {
            specification = specification.and(TourSpecification.hasStartDate(startDate));
        }

        if (min != null || max != null) {
            specification = specification.and(TourSpecification.hasPriceBetween(min, max));
        }

        if (!farmSet.isEmpty()) {
            specification = specification.and(TourSpecification.hasFarms(farmSet));
        }

        // Paginate and retrieve the filtered tours
        Page<Tour> tourPage = tourRepository.findAll(specification, PageRequest.of(page, size));
        List<TourResponse> tourResponses = new ArrayList<>();

        // Process the filtered tours
        for (Tour tour : tourPage.getContent()) {
            if (tour.getStatus().equals("OPEN") && !tour.isDeleted()) {
                TourResponse tourResponse = new TourResponse();
                tourResponse.setId(tour.getId());
                tourResponse.setTourId(tour.getTourId());
                tourResponse.setTourName(tour.getTourName());
                tourResponse.setStartDate(tour.getStartDate());
                tourResponse.setDuration(tour.getDuration());
                tourResponse.setImage(tour.getImage());
                tourResponse.setStatus(tour.getStatus());
                tourResponse.setPrice(tour.getPrice());
                tourResponse.setPerAdultPrice(tour.getPerAdultPrice());
                tourResponse.setPerChildrenPrice(tour.getPerChildrenPrice());
                tourResponse.setTime(tour.getTime());
                tourResponse.setDescription(tour.getDescription());
                tourResponse.setFarms(tour.getFarms());

                if (tour.getAccount() != null) {
                    tourResponse.setConsultingId(tour.getAccount().getId());
                }

                tourResponses.add(tourResponse);
            }
        }

        // Prepare the response object
        DataResponse<TourResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourResponses.size());
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
                tourResponse.setPrice(tour.getPrice());
                tourResponse.setPerAdultPrice(tour.getPerAdultPrice());
                tourResponse.setPerChildrenPrice(tour.getPerChildrenPrice());
                tourResponse.setTime(tour.getTime());
                tourResponse.setFarms(tour.getFarms());
                tourResponse.setDescription(tour.getDescription());
                tourResponse.setConsultingId(tour.getAccount().getId());
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
        LocalTime afternoonStart = LocalTime.of(15, 0);
        LocalTime afternoonEnd = LocalTime.of(22, 0);

        return (time.isAfter(morningStart) && time.isBefore(morningEnd)) ||
                (time.isAfter(afternoonStart) && time.isBefore(afternoonEnd));
    }

    public Tour updateTour(TourRequest tour, long TourId) {
        Account consultingAccount = accountRepository.findById(tour.getConsultingId())
                .orElseThrow(() -> new NotFoundException("Account not found ID: " + tour.getConsultingId()));
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
        oldTour.setPrice(tour.getPrice());
        oldTour.setTime(tour.getTime());
        oldTour.setDescription(tour.getDescription());
        oldTour.setAccount(consultingAccount);
        oldTour.setDeleted(tour.isDeleted());
        oldTour.setPerAdultPrice(tour.getPerAdultPrice());
        oldTour.setPerChildrenPrice(tour.getPerChildrenPrice());
        return tourRepository.save(oldTour);
    }

    public Tour deleteTour(long TourId) {
        Tour oldTour = tourRepository.findTourById(TourId);
        if (oldTour == null) {
            throw new NotFoundException("Tour not found !");
        }
        oldTour.setDeleted(true);
        return tourRepository.save(oldTour);
    }

    public Tour setOpen(long TourId) throws SchedulerException {
        Tour oldTour = tourRepository.findTourById(TourId);
        if(oldTour ==null){
            throw new NotFoundException("Tour not found !");
        }
        oldTour.setStatus("NOT OPEN");
        scheduleJob.cancelScheduledJob(TourId);
        return tourRepository.save(oldTour);
    }

    private Map<Long, ScheduleJob> scheduledJobs = new HashMap<>();
    public String scheduleTour(OpenTourRequest openTourRequest) throws Exception {
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra và lấy Tour từ cơ sở dữ liệu
        Optional<Tour> tourOpt = tourRepository.findById(openTourRequest.getId());
        if (!tourOpt.isPresent()) {
            throw new NotFoundException("Not Found ID");
        }

        Tour tour = tourOpt.get();


        // Xác nhận thời gian hợp lệ
        LocalDateTime startTime = openTourRequest.getStartTime();
        LocalDateTime endTime = openTourRequest.getEndTime();

        if (startTime.isBefore(now)) {
            throw new Exception("Start time cannot be in the past!");
        }
        if (endTime.isBefore(now)) {
            throw new Exception("End time cannot be in the past!");
        }
        if (endTime.isBefore(startTime)) {
            throw new Exception("End time cannot be before start time!");
        }

        // Hủy công việc cũ nếu tồn tại
        if (scheduledJobs.containsKey(tour.getId())) {
            ScheduleJob existingJob = scheduledJobs.get(tour.getId());
            existingJob.cancelScheduledJob(tour.getId()); // Hủy công việc đã lên lịch
            scheduledJobs.remove(tour.getId());
        }

        scheduleJob.scheduleActivation(tour.getId(), Timestamp.valueOf(startTime));
        scheduleJob.scheduleDeactivation(tour.getId(), Timestamp.valueOf(endTime));
        tour.setPrice(openTourRequest.getPrice());
        tourRepository.save(tour);

        scheduledJobs.put(tour.getId(), scheduleJob);

        return "Tour scheduling set successfully from " + startTime + " to " + endTime;

}


    public DataResponse<TourResponses>getAll(@RequestParam int page, @RequestParam int size) {
        Page<Tour> tourPage = tourRepository.findAll(PageRequest.of(page, size));
        List<Tour> tours = tourPage.getContent();
        List<TourResponses> tourResponses = new ArrayList<>();

        for (Tour tour : tours) {
            TourResponses tourResponse = new TourResponses();
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
            tourResponse.setPerAdultPrice(tour.getPerAdultPrice());
            tourResponse.setPerChildrenPrice(tour.getPerChildrenPrice());
            tourResponse.setTime(tour.getTime());
            tourResponse.setDescription(tour.getDescription());
            tourResponse.setConsultingName(tour.getAccount().getFullName());
            tourResponses.add(tourResponse);
        }
        DataResponse<TourResponses> dataResponse = new DataResponse<>();
        dataResponse.setListData(tourResponses);
        dataResponse.setTotalElements(tourPage.getTotalElements());
        dataResponse.setPageNumber(tourPage.getNumber());
        dataResponse.setTotalPages(tourPage.getTotalPages());
        return dataResponse;
    }
}
