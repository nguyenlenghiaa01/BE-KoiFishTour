package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.model.FarmRequest;
import com.example.demo.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farm")
@CrossOrigin("*")
public class FarmAPI {
    @Autowired
    FarmService farmService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody FarmRequest farm) {
        Farm newFarm = farmService.createNewFarm(farm);
        //return ve font end
        return ResponseEntity.ok(newFarm);
    }

    // Get danh sách farm
    @GetMapping
    public ResponseEntity get(){
        List<Farm> farms = farmService.getAllFarm();
        return ResponseEntity.ok(farms);
    }
    // /api/farm/{id} => id cua thang farm minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateFarm(@Valid @RequestBody Farm farm, @PathVariable long id){//valid kich hoat co che vadilation
        Farm newFarm = farmService.updateFarm(farm,id);
        return ResponseEntity.ok(newFarm);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteFarm(@PathVariable long id) {
        Farm newFarm = farmService.deleteFarm(id);
        return ResponseEntity.ok(newFarm);
    }
}
