package com.example.demo.api;

import com.example.demo.entity.DeliveringStaff;
import com.example.demo.service.DeliveringService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivering")
@CrossOrigin("*")
public class DeliveringStaffAPI {
    @Autowired
    DeliveringService deliveringService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody DeliveringStaff deliveringStaff) {
        DeliveringStaff newDelivering = deliveringService.createNewDeliveringStaff(deliveringStaff);
        //return ve font end
        return ResponseEntity.ok(newDelivering);
    }

    // Get danh sách delivering
    @GetMapping
    public ResponseEntity get(){
        List<DeliveringStaff> deliveringStaffs = deliveringService.getAllDeliveringStaff();
        return ResponseEntity.ok(deliveringStaffs);
    }
    // /api/student/{id} => id cua thang delivering minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateDelivering(@Valid @RequestBody DeliveringStaff deliveringStaff, @PathVariable long id){//valid kich hoat co che vadilation
        DeliveringStaff newDelivering = deliveringService.updateDeliveringStaff(deliveringStaff,id);
        return ResponseEntity.ok(newDelivering);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteDelivering(@PathVariable long id){
        DeliveringStaff newDelivering = deliveringService.deleteDeliveringStaff(id);
        return ResponseEntity.ok(newDelivering);
    }
}
