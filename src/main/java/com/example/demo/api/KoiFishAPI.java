package com.example.demo.api;

import com.example.demo.entity.KoiFish;
import com.example.demo.model.Request.KoiFishRequest;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.service.KoiFishService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFishAPI {
    @Autowired
    KoiFishService koiService;
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody KoiFishRequest koiFishRequest) {
        KoiFish newStudent = koiService.createNewKoi(koiFishRequest);
        //return ve font end
        return ResponseEntity.ok(newStudent);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        KoiFishResponse koiFishResponse = koiService.getAllKoi(page, size);
        return ResponseEntity.ok(koiFishResponse);
    }
    // /api/koi/{id} => id cua thang koi minh muon update
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("{id}")
    public ResponseEntity updateKoiFish(@Valid @RequestBody KoiFishRequest koi, @PathVariable long id){//valid kich hoat co che vadilation
        KoiFish newStudent = koiService.updateKoiFish(koi,id);
        return ResponseEntity.ok(newStudent);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable long id){
        KoiFish newStudent = koiService.deleteKoi(id);
        return ResponseEntity.ok(newStudent);
    }
}
