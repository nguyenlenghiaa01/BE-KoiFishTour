package com.example.demo.api;

import com.example.demo.entity.ConsultingStaff;
import com.example.demo.service.ConsultingStaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consulting")
@CrossOrigin("*")
public class ConsultingStaffAPI {
    @Autowired
    ConsultingStaffService consultingStaffService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ConsultingStaff consultingStaff) {
        ConsultingStaff newConsultingStaff = consultingStaffService.createNewConsulting(consultingStaff);
        //return ve font end
        return ResponseEntity.ok(newConsultingStaff);
    }

    // Get danh sách consulting
    @GetMapping
    public ResponseEntity get(){
        List<ConsultingStaff> consultingStaffs = consultingStaffService.getAllConsulting();
        return ResponseEntity.ok(consultingStaffs);
    }
    // /api/consulting/{id} => id cua thang consulting minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateConsultingStaff(@Valid @RequestBody ConsultingStaff consultingStaff, @PathVariable long id){//valid kich hoat co che vadilation
        ConsultingStaff newConsulting = consultingStaffService.updateConsultingStaff(consultingStaff,id);
        return ResponseEntity.ok(newConsulting);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteConsultingStaff(@PathVariable long id){
        ConsultingStaff newConsultingStaff = consultingStaffService.deleteConsultingStaff(id);
        return ResponseEntity.ok(newConsultingStaff);
    }
}
