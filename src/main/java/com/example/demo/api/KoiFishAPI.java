package com.example.demo.api;

import com.example.demo.entity.KoiFish;
import com.example.demo.service.KoiFishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/koi")
@CrossOrigin("*")
public class KoiFishAPI {
    @Autowired
    KoiFishService koiService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody KoiFish koi) {
        KoiFish newStudent = koiService.createNewKoi(koi);
        //return ve font end
        return ResponseEntity.ok(newStudent);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get(){
        List<KoiFish> kois = koiService.getAllKoi();
        return ResponseEntity.ok(kois);
    }
    // /api/koi/{id} => id cua thang koi minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateStudent(@Valid @RequestBody KoiFish koi, @PathVariable String id){//valid kich hoat co che vadilation
        KoiFish newStudent = koiService.updateStudent(koi,id);
        return ResponseEntity.ok(newStudent);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteKoi(@PathVariable String id){
        KoiFish newStudent = koiService.deleteKoi(id);
        return ResponseEntity.ok(newStudent);
    }
}
