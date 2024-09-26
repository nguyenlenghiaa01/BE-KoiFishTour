package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.entity.Sale;
import com.example.demo.service.FarmService;
import com.example.demo.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
@CrossOrigin("*")
public class SaleAPI {
    @Autowired
    SaleService saleService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Sale sale) {
        Sale newSale = saleService.createNewSale(sale);
        //return ve font end
        return ResponseEntity.ok(newSale);
    }

    // Get danh sách farm
    @GetMapping
    public ResponseEntity get(){
        List<Sale> sales = saleService.getAllSale();
        return ResponseEntity.ok(sales);
    }
    // /api/sale/{id} => id cua thang sale minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateSale(@Valid @RequestBody Sale sale, @PathVariable long id){//valid kich hoat co che vadilation
        Sale newSale = saleService.updateSale(sale,id);
        return ResponseEntity.ok(newSale);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteSale(@PathVariable long id) {
        Sale newSale = saleService.deleteSale(id);
        return ResponseEntity.ok(newSale);
    }
}
