package com.example.demo.api;

import com.example.demo.entity.CustomTour;
import com.example.demo.model.Request.CustomTourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.CustomerTourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customTour")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class CustomTourAPI {
    @Autowired
    CustomerTourService customerTourService;
    @GetMapping
    public ResponseEntity<?>create(@Valid @RequestBody CustomTourRequest customTourRequest){
        CustomTour customTour = customerTourService.createNewCus(customTourRequest);
        return ResponseEntity.ok(customTour);
    }

    @PostMapping("/get")
    public ResponseEntity<?>get(@RequestParam int page,@RequestParam int size ){
        DataResponse dataResponse = customerTourService.getAllCus(page,size);
        return ResponseEntity.ok(dataResponse);
    }
}
