package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.TourRequest;
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
        try {
            Tour newTour = tourService.createNewTour(tourRequest);
            return ResponseEntity.ok(newTour);
        } catch (DuplicateEntity e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        TourResponse tourResponse = tourService.getAllTour(page, size);
        return ResponseEntity.ok(tourResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateTour(@Valid @RequestBody TourRequest tourRequest, @PathVariable long id) {
        try {
            Tour updatedTour = tourService.updateTour(tourRequest, id);
            return ResponseEntity.ok(updatedTour);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTour(@PathVariable long id) {
        try {
            Tour deletedTour = tourService.deleteTour(id);
            return ResponseEntity.ok(deletedTour);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

