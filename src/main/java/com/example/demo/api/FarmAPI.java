package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.FarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farm")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FarmAPI {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private FarmService farmService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FarmRequest farmRequest) {
        Farm newFarm = farmService.createNewFarm(farmRequest);
        simpMessagingTemplate.convertAndSend("topic/farm","CREATE NEW FARM");

        return ResponseEntity.ok(newFarm);
    }

    @GetMapping("/guest/get")
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = farmService.getAllFarm(page, size);
        return ResponseEntity.ok(dataResponse);
    }
    @GetMapping
    public ResponseEntity<Farm> get(long id){
        Farm farm = farmService.getFarmById(id);
        return ResponseEntity.ok(farm);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateFarm(@Valid @RequestBody FarmRequest farm, @PathVariable long id) {
            Farm updatedFarm = farmService.updateFarm(farm, id);
        simpMessagingTemplate.convertAndSend("topic/farm","UPDATE FARM");

            return ResponseEntity.ok(updatedFarm);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFarm(@PathVariable long id) {
            Farm deletedFarm = farmService.deleteFarm(id);
        simpMessagingTemplate.convertAndSend("topic/farm","DELETE FARM");
            return ResponseEntity.ok(deletedFarm);
    }
}
