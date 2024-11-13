package com.example.demo.api;

import com.example.demo.entity.Breed;
import com.example.demo.model.Request.BreedRequest;
import com.example.demo.model.Response.BreedResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.BreedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breed")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BreedAPI {

    @Autowired
    BreedService breedService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Breed> create(@Valid @RequestBody BreedRequest breedRequest) {
        Breed newBreed = breedService.createNewBreed(breedRequest);
        simpMessagingTemplate.convertAndSend("topic/breed","CREATE NEW BREED");
        return ResponseEntity.ok(newBreed);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<DataResponse<BreedResponse>> get(@RequestParam int page, @RequestParam int size){
        DataResponse<BreedResponse> dataResponse = breedService.getAllBreed(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Breed> updateBreed(@Valid @RequestBody BreedRequest breedRequest, @PathVariable long id) {
            Breed updatedBreed = breedService.updateBreed(breedRequest, id);
            return ResponseEntity.ok(updatedBreed);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Breed> deleteBreed(@PathVariable long id) {
            Breed deletedBreed = breedService.deleteBreed(id);
            return ResponseEntity.ok(deletedBreed);
    }
}

