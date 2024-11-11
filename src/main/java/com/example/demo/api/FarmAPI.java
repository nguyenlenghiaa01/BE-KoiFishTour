package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FarmResponse;
import com.example.demo.service.FarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

        //return ve font end
        return ResponseEntity.ok(newFarm);
    }

    // Lấy farm
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

    // Cập nhật farm
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateFarm(@Valid @RequestBody FarmRequest farm, @PathVariable long id) {
            Farm updatedFarm = farmService.updateFarm(farm, id);
        simpMessagingTemplate.convertAndSend("topic/farm","UPDATE FARM");

            return ResponseEntity.ok(updatedFarm);
    }

    // Xóa farm
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFarm(@PathVariable long id) {
            Farm deletedFarm = farmService.deleteFarm(id);
        simpMessagingTemplate.convertAndSend("topic/farm","DELETE FARM");
            return ResponseEntity.ok(deletedFarm);
    }
}
