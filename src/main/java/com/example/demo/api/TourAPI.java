package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long id) {
            Tour updatedTour = tourService.updateTour(tourRequest, id);
            return ResponseEntity.ok(updatedTour);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTour(@PathVariable long id) {
            Tour deletedTour = tourService.deleteTour(id);
            return ResponseEntity.ok(deletedTour);
    }
}

