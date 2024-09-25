package com.example.demo.api;

import com.example.demo.entity.OpenTour;
import com.example.demo.entity.OrderCart;
import com.example.demo.service.OpenTourService;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/OpenTour")
@RestController
@CrossOrigin("*")
public class OpenTourAPI {
    @Autowired
    OpenTourService openTourService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody OpenTour openTour) {
        OpenTour newOpenTour = openTourService.createNewOpenTour(openTour);
        //return ve font end
        return ResponseEntity.ok(newOpenTour);
    }

    // Get danh sách order
    @GetMapping
    public ResponseEntity get(){
        List<OpenTour> orders = openTourService.getAllOpenTour();
        return ResponseEntity.ok(orders);
    }
    // /api/order/{id} => id cua thang order minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateOpenTour(@Valid @RequestBody OpenTour openTour, @PathVariable long id){//valid kich hoat co che vadilation
        OpenTour newOpenTour = openTourService.updateOpenTour(openTour,id);
        return ResponseEntity.ok(newOpenTour);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteOpenTour(@PathVariable long id) {
        OpenTour newOpenTour = openTourService.deleteOpenTour(id);
        return ResponseEntity.ok(newOpenTour);
    }
}
