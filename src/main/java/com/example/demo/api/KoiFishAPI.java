package com.example.demo.api;

import com.example.demo.entity.KoiFish;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.KoiFishRequest;
import com.example.demo.model.Response.DataResponse;
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
            KoiFish newKoiFish = koiService.createNewKoi(koiFishRequest);
            return ResponseEntity.ok(newKoiFish);
    }

    // Get danh sách cá Koi
    @GetMapping("/guest/get")
    public ResponseEntity<?> get(@RequestParam int page, @RequestParam int size){
        DataResponse<?> dataResponse = koiService.getAllKoi(page, size);
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/listKoiFish")
    public ResponseEntity<?> getList(@RequestParam int page,@RequestParam int size ,long id){
        DataResponse<KoiFish> koiFishDataResponse = koiService.getListKoiFish(page, size, id);
        return ResponseEntity.ok(koiFishDataResponse);
    }

    // Cập nhật cá Koi
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateKoiFish(@Valid @RequestBody KoiFishRequest koi, @PathVariable long id) {
            KoiFish updatedKoiFish = koiService.updateKoiFish(koi, id);
            return ResponseEntity.ok(updatedKoiFish);
    }

    // Xóa cá Koi
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteKoi(@PathVariable long id) {
            KoiFish deletedKoiFish = koiService.deleteKoi(id);
            return ResponseEntity.ok(deletedKoiFish);
    }
}
