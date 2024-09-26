package com.example.demo.api;

import com.example.demo.entity.Manager;
import com.example.demo.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin("*")
public class ManagerAPI {
    @Autowired
    ManagerService managerService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Manager manager) {
        Manager newManager = managerService.createNewManager(manager);
        //return ve font end
        return ResponseEntity.ok(newManager);
    }

    // Get danh sách farm
    @GetMapping
    public ResponseEntity get(){
        List<Manager> managers = managerService.getAllManager();
        return ResponseEntity.ok(managers);
    }
    // /api/manager/{id} => id cua thang manager minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateManager(@Valid @RequestBody Manager manager, @PathVariable long id){//valid kich hoat co che vadilation
        Manager newManager = managerService.updateManager(manager,id);
        return ResponseEntity.ok(newManager);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteManager(@PathVariable long id) {
        Manager newManager = managerService.deleteManager(id);
        return ResponseEntity.ok(newManager);
    }
}
