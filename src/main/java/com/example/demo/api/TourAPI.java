package com.example.demo.api;

import com.example.demo.entity.Tour;
import com.example.demo.service.TourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
@CrossOrigin("*")
public class TourAPI {
    @Autowired
    TourService tourService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Tour tour) {
        Tour newTour = tourService.createNewTour(tour);
        //return ve font end
        return ResponseEntity.ok(newTour);
    }

    // Get danh sách consulting
    @GetMapping
    public ResponseEntity get(){
        List<Tour> tours = tourService.getAllTour();
        return ResponseEntity.ok(tours);
    }
    // /api/tour/{id} => id cua thang tour minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateTour(@Valid @RequestBody Tour tour, @PathVariable long id){//valid kich hoat co che vadilation
        Tour newTour = tourService.updateTour(tour,id);
        return ResponseEntity.ok(newTour);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteTour(@PathVariable long id){
        Tour newTour = tourService.deleteTour(id);
        return ResponseEntity.ok(newTour);
    }
}
