package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.model.Response.TourResponses;
import com.example.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TourAPI {

    @Autowired
    private TourService tourService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<Tour> create(@Valid @RequestBody TourRequest tourRequest) {
            Tour newTour = tourService.createNewTour(tourRequest);
        simpMessagingTemplate.convertAndSend("/topic/tour","CREATE SCHEDULE");

            return ResponseEntity.ok(newTour);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<DataResponse<TourResponse>> get(@RequestParam int page, @RequestParam int size){
        DataResponse<TourResponse> dataResponse = tourService.getAllTour(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/manager/get/notOpen")
    public ResponseEntity<DataResponse<TourResponse>> getTour(@RequestParam int page, @RequestParam int size){
        DataResponse<TourResponse> dataResponse = tourService.getAllTourNotOpen(page, size);
        simpMessagingTemplate.convertAndSend("topic/tour","CLOSE TOUR");
        return ResponseEntity.ok(dataResponse);
    }
    @PostMapping("/setOpen")
    public ResponseEntity<Tour> setOpen (long id) throws SchedulerException {
        Tour tour = tourService.setOpen(id);
        simpMessagingTemplate.convertAndSend("/topic/tour","OPEN TOUR");
        return  ResponseEntity.ok(tour);
    }

    @GetMapping("/consulting")
    public ResponseEntity<DataResponse<TourResponse>> getTourByConsulting(@RequestParam String consulId, @RequestParam int page, @RequestParam int size) {
        DataResponse<TourResponse> dataResponse = tourService.getTourByConsulting(consulId, page, size);
        return ResponseEntity.ok(dataResponse);
    }


    @GetMapping("/get/all")
    public ResponseEntity<DataResponse<TourResponses>> getAll(@RequestParam int page, @RequestParam int size){
        DataResponse<TourResponses> dataResponse = tourService.getAll(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping
    public ResponseEntity<Tour>getTourById(String id){
        Tour newTour = tourService.getTourId(id);
        return ResponseEntity.ok(newTour);
    }

    @GetMapping("/search/first")
    public DataResponse<TourResponse> searchTours(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) String farm,
            @RequestParam int page,
            @RequestParam int size
    ) {
        BigDecimal minPrice = (min != null && min != 0) ? BigDecimal.valueOf(min) : null;
        BigDecimal maxPrice = (max != null && max != 0) ? BigDecimal.valueOf(max) : null;

        Set<String> farmSet = new HashSet<>();
        if (farm != null && !farm.isEmpty()) {
            String[] farms = farm.split(",");
            for (String f : farms) {
                farmSet.add(f.trim());
            }
        }

        return tourService.searchTours(page, size, startDate, minPrice, maxPrice, farmSet);
    }



    @GetMapping("/search/second")
    public DataResponse<TourResponse> getAllTourPrice(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String time) {

        return tourService.getAllTourPrice(page, size, minPrice, maxPrice, time);
    }


    @PutMapping("{id}")
    public ResponseEntity<Tour> updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long id) {
            Tour updatedTour = tourService.updateTour(tourRequest, id);
            return ResponseEntity.ok(updatedTour);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Tour> deleteTour(@PathVariable long id) {
            Tour deletedTour = tourService.deleteTour(id);
            return ResponseEntity.ok(deletedTour);
    }


    @PostMapping("/schedule")
    public String scheduleTour(OpenTourRequest openTourRequest) {
        try {
            simpMessagingTemplate.convertAndSend("/topic/tour","SET SCHEDULE OPEN TOUR");
            return tourService.scheduleTour(openTourRequest);
        } catch (Exception e) {
            return "Error during scheduling: " + e.getMessage();
        }
    }
}

