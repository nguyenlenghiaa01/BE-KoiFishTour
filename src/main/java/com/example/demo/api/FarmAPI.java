package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.model.Request.FarmRequest;
import com.example.demo.model.Response.FarmResponse;
import com.example.demo.service.FarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    FarmService farmService;
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody FarmRequest farmRequest) {
        Farm newFarm = farmService.createNewFarm(farmRequest);
        //return ve font end
        return ResponseEntity.ok(newFarm);
    }

    // Get danh sách farm
    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        FarmResponse farmResponse = farmService.getAllFarm(page, size);
        return ResponseEntity.ok(farmResponse);
    }
    // /api/farm/{id} => id cua thang farm minh muon update
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity updateFarm(@Valid @RequestBody FarmRequest farm, @PathVariable long id){//valid kich hoat co che vadilation
        Farm newFarm = farmService.updateFarm(farm,id);
        return ResponseEntity.ok(newFarm);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity deleteFarm(@PathVariable long id) {
        Farm newFarm = farmService.deleteFarm(id);
        return ResponseEntity.ok(newFarm);
    }
}
