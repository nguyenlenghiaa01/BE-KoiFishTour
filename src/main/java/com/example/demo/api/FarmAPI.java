package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.service.FarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farm")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FarmAPI {

    @Autowired
    private FarmService farmService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FarmRequest farmRequest) {
        Farm newFarm = farmService.createNewFarm(farmRequest);
        return ResponseEntity.ok(newFarm);
    }

    // Lấy farm
    @GetMapping
    public ResponseEntity<List<Farm>> get() {
        List<Farm> farms = farmService.getAllFarm();
        return ResponseEntity.ok(farms);
    }

    // Cập nhật farm
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateFarm(@Valid @RequestBody FarmRequest farm, @PathVariable long id) {
        try {
            Farm updatedFarm = farmService.updateFarm(farm, id);
            return ResponseEntity.ok(updatedFarm);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Xóa farm
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFarm(@PathVariable long id) {
        try {
            Farm deletedFarm = farmService.deleteFarm(id);
            return ResponseEntity.ok(deletedFarm);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
