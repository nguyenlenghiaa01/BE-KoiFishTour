package com.example.demo.api;

import com.example.demo.entity.OpenTour;
import com.example.demo.model.Request.OpenToursRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.OpenToursResponse;
import com.example.demo.service.OpenTourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/open-tour")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class OpenTourAPI {
    @Autowired
    OpenTourService openTourService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<OpenTour> create(@Valid @RequestBody OpenToursRequest openToursRequest) {
        OpenTour newOpenTour = openTourService.createNewOpenTour(openToursRequest);
        simpMessagingTemplate.convertAndSend("topic/openTour","CREATE OpenTour");
        return ResponseEntity.ok(newOpenTour);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<DataResponse<OpenToursResponse>> get(@RequestParam int page, @RequestParam int size){
        DataResponse<OpenToursResponse> dataResponse = openTourService.getAllOpenTour(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<OpenTour> updateOpenTour(@Valid @RequestBody String status, double price, @PathVariable long id) {
        OpenTour updateOpenTour = openTourService.updateOpenTour(status,price, id);
        return ResponseEntity.ok(updateOpenTour);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<OpenTour> deleteOpenTour(@PathVariable long id) {
        OpenTour deletedOpenTour = openTourService.deleteOpenTour(id);
        return ResponseEntity.ok(deletedOpenTour);
    }
}
