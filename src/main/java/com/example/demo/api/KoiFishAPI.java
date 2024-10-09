package com.example.demo.api;

import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
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
    private KoiFishService koiService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody KoiFishRequest koiFishRequest) {
        try {
            KoiFish newKoiFish = koiService.createNewKoi(koiFishRequest);
            return ResponseEntity.ok(newKoiFish);
        } catch (DuplicateEntity e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get danh sách cá Koi
    @GetMapping
    public ResponseEntity get(@RequestParam int page, @RequestParam int size){
        KoiFishResponse koiFishResponse = koiService.getAllKoi(page, size);
        return ResponseEntity.ok(koiFishResponse);
    }

    // Cập nhật cá Koi
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateKoiFish(@Valid @RequestBody KoiFishRequest koi, @PathVariable long id) {
        try {
            KoiFish updatedKoiFish = koiService.updateKoiFish(koi, id);
            return ResponseEntity.ok(updatedKoiFish);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Xóa cá Koi
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteKoi(@PathVariable long id) {
        try {
            KoiFish deletedKoiFish = koiService.deleteKoi(id);
            return ResponseEntity.ok(deletedKoiFish);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
