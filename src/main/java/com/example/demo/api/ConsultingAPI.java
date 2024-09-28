package com.example.demo.api;

import com.example.demo.entity.Consulting;
import com.example.demo.model.ConsultingRequest;
import com.example.demo.service.ConsultingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulting")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ConsultingAPI {
    @Autowired
    ConsultingService consultingService;


    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ConsultingRequest consultingRequest) {
        // nhờ service tạo mới 1 account
        Consulting newConsulting = consultingService.createNewConsulting(consultingRequest);
        // return về cho front-end
        return ResponseEntity.ok(newConsulting);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get() {
        List<Consulting> consultings = consultingService.getAllConsulting();
        return ResponseEntity.ok(consultings);
    }

    // /api/consulting/{id} => id của thằng consulting mình muốn update
    @PutMapping("{id}")
    public ResponseEntity updateConsulting(@Valid @RequestBody Consulting consulting, @PathVariable long id) {
        Consulting newConsulting = consultingService.updateConsulting(consulting,id);
        return ResponseEntity.ok(newConsulting);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteConsulting(@PathVariable long id) {
        Consulting newConsulting = consultingService.deleteConsulting(id);
        return ResponseEntity.ok(newConsulting);
    }
}
