package com.example.demo.api;

import com.example.demo.entity.OpenTour;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.service.OpenTourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/OpenTour")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@PreAuthorize("hasAuthority('MANAGER')")
public class OpenTourAPI {
    @Autowired
    OpenTourService openTourService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody OpenTourRequest openTourRequest) {
        OpenTour newOpenTour = openTourService.createNewOpenTour(openTourRequest);
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
    public ResponseEntity updateOpenTour(@Valid @RequestBody OpenTourRequest openTour, @PathVariable long id){//valid kich hoat co che vadilation
        OpenTour newOpenTour = openTourService.updateOpenTour(openTour,id);
        return ResponseEntity.ok(newOpenTour);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteOpenTour(@PathVariable long id) {
        OpenTour newOpenTour = openTourService.deleteOpenTour(id);
        return ResponseEntity.ok(newOpenTour);
    }
}
