package com.example.demo.api;

import com.example.demo.entity.QuotationProcess;
import com.example.demo.model.Request.QuotationProcessRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.QuotationProcessService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @PreAuthorize("hasAuthority('SALE')")
    @PostMapping
    public ResponseEntity<QuotationProcess> create(@Valid @RequestBody QuotationProcessRequest quotationProcessRequest) {
        QuotationProcess newQuotationProcess = quotationProcessService.createNewQuotationProcess(quotationProcessRequest);
        return ResponseEntity.ok(newQuotationProcess);
    }
    @PreAuthorize("hasAuthority('SALE','ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<DataResponse<QuotationProcess>> get(int page , int size,long id){
        DataResponse<QuotationProcess> quotationProcesses = quotationProcessService.getAllQuotation(page, size, id);
        return ResponseEntity.ok(quotationProcesses);
    }
    @PreAuthorize("hasAuthority('SALE')")
    @PutMapping("{id}")
    public ResponseEntity<QuotationProcess> updateQuotationProcess(@Valid @RequestBody QuotationProcessRequest quotationProcess, @PathVariable long id){//valid kich hoat co che vadilation
        QuotationProcess newQuotationProcess = quotationProcessService.updateQuotationProcess(quotationProcess,id);
        return ResponseEntity.ok(newQuotationProcess);
    }
    @PreAuthorize("hasAuthority('SALE')")
    @DeleteMapping("{id}")
    public ResponseEntity<QuotationProcess> deleteQuotationProcess(@PathVariable long id){
        QuotationProcess newQuotationProcess = quotationProcessService.deleteQuotationProcess(id);
        return ResponseEntity.ok(newQuotationProcess);
    }

}
