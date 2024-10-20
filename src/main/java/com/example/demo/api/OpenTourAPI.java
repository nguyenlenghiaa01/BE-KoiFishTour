package com.example.demo.api;

import com.example.demo.entity.OpenTour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.OpenTourRequest;
import com.example.demo.service.OpenTourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/OpenTour")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class OpenTourAPI {

    @Autowired
    private OpenTourService openTourService;

//    @PreAuthorize("hasAuthority('MANAGER')")
//    @PostMapping
//    public ResponseEntity<?> create(@Valid @RequestBody OpenTourRequest openTourRequest) {
//            OpenTour newOpenTour = openTourService.createNewOpenTour(openTourRequest);
//            return ResponseEntity.ok(newOpenTour);
//    }

    // Get danh sách tour mở
    @GetMapping
    public ResponseEntity<List<OpenTour>> get() {
        List<OpenTour> openTours = openTourService.getAllOpenTour();
        return ResponseEntity.ok(openTours);
    }

//    // Cập nhật tour mở
//    @PreAuthorize("hasAuthority('MANAGER')")
//    @PutMapping("/open/{tourId}")
//    public ResponseEntity<?> updateOpenTour(@Valid @RequestBody OpenTourRequest openTourRequest, @PathVariable long id) {
//            OpenTour updatedOpenTour = openTourService.updateOpenTour(openTourRequest, id);
//            return ResponseEntity.ok(updatedOpenTour);
//    }

    // Xóa tour mở
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOpenTour(@PathVariable long id) {
            OpenTour deletedOpenTour = openTourService.deleteOpenTour(id);
            return ResponseEntity.ok(deletedOpenTour);
    }
}
