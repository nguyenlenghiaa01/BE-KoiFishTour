package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TourAPI {

    @Autowired
    private TourService tourService;

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PutMapping("{tourId}")
    public ResponseEntity<?> openTour(@Valid @RequestBody OpenTourRequest openTourRequest, @PathVariable long tourId) {
        Tour updatedTour = tourService.opentour(openTourRequest, tourId);
        return ResponseEntity.ok(updatedTour);
    }



    @PutMapping("{id}/updateTour")
    public ResponseEntity<?> updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long id) {
            Tour updatedTour = tourService.updateTour(tourRequest, id);
            return ResponseEntity.ok(updatedTour);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTour(@PathVariable long id) {
            Tour deletedTour = tourService.deleteTour(id);
            return ResponseEntity.ok(deletedTour);
    }
}

