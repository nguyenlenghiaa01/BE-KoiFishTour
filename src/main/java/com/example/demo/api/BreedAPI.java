package com.example.demo.api;

import com.example.demo.entity.Breed;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BreedRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.BreedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breed")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BreedAPI {

    @Autowired
    private BreedService breedService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BreedRequest breedRequest) {
        Breed newBreed = breedService.createNewBreed(breedRequest);
        return ResponseEntity.ok(newBreed);
    }

    // Lấy danh sách breed
    @GetMapping("/guest/get")
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = breedService.getAllBreed(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    // Cập nhật breed
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateBreed(@Valid @RequestBody BreedRequest breedRequest, @PathVariable long id) {
            Breed updatedBreed = breedService.updateBreed(breedRequest, id);
            return ResponseEntity.ok(updatedBreed);
    }

    // Xóa breed
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBreed(@PathVariable long id) {
            Breed deletedBreed = breedService.deleteBreed(id);
            return ResponseEntity.ok(deletedBreed);
    }
}

