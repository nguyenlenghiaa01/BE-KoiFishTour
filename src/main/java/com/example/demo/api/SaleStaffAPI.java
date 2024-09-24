package com.example.demo.api;

import com.example.demo.entity.SaleStaff;
import com.example.demo.service.SaleStaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleStaffAPI {
    @Autowired
    SaleStaffService saleStaffService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody SaleStaff saleStaff) {
        SaleStaff newSaleStaff = saleStaffService.createNewSaleStaff(saleStaff);
        //return ve font end
        return ResponseEntity.ok(newSaleStaff);
    }

    // Get danh sách sale
    @GetMapping
    public ResponseEntity get(){
        List<SaleStaff> saleStaffs = saleStaffService.getAllSaleStaff();
        return ResponseEntity.ok(saleStaffs);
    }
    // /api/sale/{id} => id cua thang sale minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateSaleStaff(@Valid @RequestBody SaleStaff saleStaff, @PathVariable long id){//valid kich hoat co che vadilation
        SaleStaff newSaleStaff = saleStaffService.updateSaleStaff(saleStaff,id);
        return ResponseEntity.ok(newSaleStaff);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteSaleStaff(@PathVariable long id){
        SaleStaff newSaleStaff = saleStaffService.deleteSaleStaff(id);
        return ResponseEntity.ok(newSaleStaff);
    }
}
