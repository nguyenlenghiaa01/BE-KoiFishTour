package com.example.demo.api;

import com.example.demo.entity.Admin;
import com.example.demo.model.AdminRequest;
import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AdminAPI {
    @Autowired
    AdminService adminService;

    // Thêm sinh viên mới
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody AdminRequest adminRequest) {
        // nhờ service tạo mới 1 account
        Admin newAdmin = adminService.createNewAdmin(adminRequest);
        // return về cho front-end
        return ResponseEntity.ok(newAdmin);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get() {
        List<Admin> students = adminService.getAllAdmin();
        return ResponseEntity.ok(students);
    }

    // /api/admin/{id} => id của thằng admin mình muốn update
    @PutMapping("{id}")
    public ResponseEntity updateAdmin(@Valid @RequestBody Admin admin, @PathVariable long id) {
        Admin newAdmin = adminService.updateAdmin(admin,id);
        return ResponseEntity.ok(newAdmin);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteAdmin(@PathVariable long id) {
        Admin newAdmin = adminService.deleteAdmin(id);
        return ResponseEntity.ok(newAdmin);
    }
}
