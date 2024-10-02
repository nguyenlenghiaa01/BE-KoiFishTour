package com.example.demo.api;

import com.example.demo.entity.Breed;
import com.example.demo.model.BreedRequest;
import com.example.demo.service.BreedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breed")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BreedAPI {
    @Autowired
    BreedService breedService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody BreedRequest breed) {
        Breed newBreed = breedService.createNewBreed(breed);
        //rBreedRequest newBreed = breedService.createNewBreed(breed);eturn ve font end
        return ResponseEntity.ok(newBreed);
    }

    // Get danh sách breed
    @GetMapping
    public ResponseEntity get(){
        List<Breed> breeds = breedService.getAllBreed();
        return ResponseEntity.ok(breeds);
    }
    // /api/breed/{id} => id cua thang breed minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateBreed(@Valid @RequestBody Breed breed, @PathVariable long id){//valid kich hoat co che vadilation
        Breed newBreed = breedService.updateBreed(breed,id);
        return ResponseEntity.ok(newBreed);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteBreed(@PathVariable long id){
        Breed newBreed = breedService.deleteBreed(id);
        return ResponseEntity.ok(newBreed);
    }
}
