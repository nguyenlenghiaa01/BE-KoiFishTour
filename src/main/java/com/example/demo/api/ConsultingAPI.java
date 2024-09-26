package com.example.demo.api;

import com.example.demo.entity.Consulting;
import com.example.demo.entity.Farm;
import com.example.demo.service.ConsultingService;
import com.example.demo.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulting")
@CrossOrigin("*")
public class ConsultingAPI {
    @Autowired
    ConsultingService consultingService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Consulting consulting) {
        Consulting newConsulting = consultingService.createNewConsulting(consulting);
        //return ve font end
        return ResponseEntity.ok(newConsulting);
    }

    // Get danh sách farm
    @GetMapping
    public ResponseEntity get(){
        List<Consulting> consultings = consultingService.getAllConsulting();
        return ResponseEntity.ok(consultings);
    }
    // /api/consulting/{id} => id cua thang consulting minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateConsulting(@Valid @RequestBody Consulting consulting, @PathVariable long id){//valid kich hoat co che vadilation
        Consulting newConsulting = consultingService.updateConsulting(consulting,id);
        return ResponseEntity.ok(newConsulting);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteConsulting(@PathVariable long id) {
        Consulting newConsulting = consultingService.deleteConsulting(id);
        return ResponseEntity.ok(newConsulting);
    }
}
