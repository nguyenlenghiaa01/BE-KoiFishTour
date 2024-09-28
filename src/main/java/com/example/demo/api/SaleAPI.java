package com.example.demo.api;

import com.example.demo.entity.Sale;
import com.example.demo.model.SaleRequest;
import com.example.demo.service.SaleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
@CrossOrigin("*")
@SecurityRequirement(name="api")
public class SaleAPI {
    @Autowired
    SaleService saleService;

    @PostMapping

    public ResponseEntity create(@Valid @RequestBody SaleRequest saleRequest) {
        // nhờ service tạo mới 1 account
        Sale newSale = saleService.createNewSale(saleRequest);
        // return về cho front-end
        return ResponseEntity.ok(saleRequest);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get() {
        List<Sale> students = saleService.getAllSale();
        return ResponseEntity.ok(students);
    }

    // /api/student/{id} => id của thằng student mình muốn update
    @PutMapping("{id}")
    public ResponseEntity updateSale(@Valid @RequestBody Sale sale, @PathVariable long id) {
        Sale newSale = saleService.updateSale(sale,id);
        return ResponseEntity.ok(newSale);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteSale(@PathVariable long id) {
        Sale newSale = saleService.deleteSale(id);
        return ResponseEntity.ok(newSale);
    }
}
