package com.example.demo.api;

import com.example.demo.entity.CustomTour;
import com.example.demo.model.Request.CustomTourRequest;
import com.example.demo.model.Response.CustomTourResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.CustomTourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customTour")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class CustomTourAPI {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    CustomTourService customTourService;
    @PostMapping
    public ResponseEntity<?>create(@Valid @RequestBody CustomTourRequest customTourRequest){
        CustomTour customTour = customTourService.createNewCus(customTourRequest);
        simpMessagingTemplate.convertAndSend("topic/customTour","CREATE NEW CUSTOM TOUR");
        return ResponseEntity.ok(customTour);
    }

    @GetMapping("/get")
    public ResponseEntity<?>get(@RequestParam int page,@RequestParam int size ){
        DataResponse<CustomTourResponse> dataResponse = customTourService.getAllCus(page,size);
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCustom(@Valid @RequestBody CustomTourRequest customTourRequest, @PathVariable long id) {
        CustomTour cus = customTourService.updateCus(customTourRequest, id);
        simpMessagingTemplate.convertAndSend("topic/customTour","UPDATE CUSTOM TOUR");
        return ResponseEntity.ok(cus);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustom(@PathVariable long id) {
        CustomTour customTour = customTourService.deleteCus(id);
        simpMessagingTemplate.convertAndSend("topic/customTour","DELETE CUSTOM TOUR");
        return ResponseEntity.ok(customTour);
    }
}
