package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TourAPI {

    @Autowired
    private TourService tourService;



    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TourRequest tourRequest) {
            Tour newTour = tourService.createNewTour(tourRequest);
            return ResponseEntity.ok(newTour);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<?> get(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = tourService.getAllTour(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/manager/get/notOpen")
    public ResponseEntity<?> getTour(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = tourService.getAllTourNotOpen(page, size);
        return ResponseEntity.ok(dataResponse);
    }
    @PostMapping("/setOpen")
    public ResponseEntity<Tour> setOpen (long id){
        Tour tour = tourService.setOpen(id);
        return  ResponseEntity.ok(tour);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = tourService.getAll(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping
    public ResponseEntity<?>getTourById(String id){
        Tour newTour = tourService.getTourId(id);
        return ResponseEntity.ok(newTour);
    }

    @GetMapping("/search/first")
    public DataResponse<TourResponse> searchTours(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String farm,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return tourService.searchTours(startDate, duration, farm, page, size);
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
    public ResponseEntity<?> updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long id) {
            Tour updatedTour = tourService.updateTour(tourRequest, id);
            return ResponseEntity.ok(updatedTour);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTour(@PathVariable long id) {
            Tour deletedTour = tourService.deleteTour(id);
            return ResponseEntity.ok(deletedTour);
    }


    @PostMapping("/schedule")
    public String scheduleTour(OpenTourRequest openTourRequest) {
        try {
            return tourService.scheduleTour(openTourRequest);
        } catch (Exception e) {
            return "Error during scheduling: " + e.getMessage();
        }
    }
}

