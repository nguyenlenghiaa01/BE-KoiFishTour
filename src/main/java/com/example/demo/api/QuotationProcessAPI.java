package com.example.demo.api;

import com.example.demo.entity.QuotationProcess;
import com.example.demo.entity.Tour;
import com.example.demo.model.Request.QuotationProcessRequest;
import com.example.demo.service.QuotationProcessService;
import com.example.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotationProcess")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@PreAuthorize("hasAuthority('MANAGER')")
public class QuotationProcessAPI {
    @Autowired
    QuotationProcessService quotationProcessService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody QuotationProcessRequest quotationProcessRequest) {
        QuotationProcess newQuotationProcess = quotationProcessService.createNewQuotationProcess(quotationProcessRequest);
        //return ve font end
        return ResponseEntity.ok(quotationProcessRequest);
    }

    // Get danh sách quotation Process
    @GetMapping
    public ResponseEntity get(){
        List<QuotationProcess> quotationProcesses = quotationProcessService.getAllQuotationProcess();
        return ResponseEntity.ok(quotationProcesses);
    }
    // /api/tour/{id} => id cua thang tour minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateQuotationProcess(@Valid @RequestBody QuotationProcessRequest quotationProcess, @PathVariable long id){//valid kich hoat co che vadilation
        QuotationProcess newQuotationProcess = quotationProcessService.updateQuotationProcess(quotationProcess,id);
        return ResponseEntity.ok(newQuotationProcess);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteQuotationProcess(@PathVariable long id){
        QuotationProcess newQuotationProcess = quotationProcessService.deleteQuotationProcess(id);
        return ResponseEntity.ok(newQuotationProcess);
    }

}
