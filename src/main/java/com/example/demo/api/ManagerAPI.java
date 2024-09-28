package com.example.demo.api;

import com.example.demo.entity.Manager;
import com.example.demo.model.ManagerRequest;
import com.example.demo.service.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/manager")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class ManagerAPI {
    @Autowired
    ManagerService managerService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ManagerRequest managerRequest) {
        // nhờ service tạo mới 1 account
        Manager newManager = managerService.createNewManager(managerRequest);
        // return về cho front-end
        return ResponseEntity.ok(newManager);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get() {
        List<Manager> managers = managerService.getAllManager();
        return ResponseEntity.ok(managers);
    }

    // /api/manager/{id} => id của thằng manager mình muốn update
    @PutMapping("{id}")
    public ResponseEntity updateManager(@Valid @RequestBody Manager manager, @PathVariable long id) {
        Manager newManager = managerService.updateManager(manager,id);
        return ResponseEntity.ok(newManager);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteManager(@PathVariable long id) {
        Manager newManager = managerService.deleteManager(id);
        return ResponseEntity.ok(newManager);
    }
}
