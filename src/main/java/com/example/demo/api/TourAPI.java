package com.example.demo.api;

import com.example.demo.entity.Tour;
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
    TourService tourService;

//    @PostMapping
//    public ResponseEntity create(@Valid @RequestBody Tour tour) {
//        Tour newTour = tourService.createNewTour(tour);
//        //return ve font end
//        return ResponseEntity.ok(newTour);
//    }
@PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TourRequest tourRequest) {
        Tour newTour = tourService.createNewTour(tourRequest);
        //return ve font end
        return ResponseEntity.ok(newTour);
    }

    // Get danh sách tour

    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        TourResponse tourResponse = tourService.getAllTour(page, size);
        return ResponseEntity.ok(tourResponse);
    }
    // /api/tour/{id} => id cua thang tour minh muon update
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity updateTour(@Valid @RequestBody TourRequest tour, @PathVariable long id){//valid kich hoat co che vadilation
        Tour newTour = tourService.updateTour(tour,id);
        return ResponseEntity.ok(newTour);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity deleteTour(@PathVariable long id){
        Tour newTour = tourService.deleteTour(id);
        return ResponseEntity.ok(newTour);
    }
}
