package com.example.demo.api;

import com.example.demo.entity.Quotation;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.service.QuotationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/quotation")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class QuotationAPI {
    @Autowired
    QuotationService quotationService;

    @PostMapping
    public ResponseEntity createQuotation(@Valid @RequestBody QuotationRequest quotationRequest) {
        Quotation newQuotation = quotationService.createQuotation(quotationRequest);
        return ResponseEntity.ok(newQuotation);
    }

    @GetMapping
    public ResponseEntity getAllQuotation() {
        List<Quotation> quotations = quotationService.getAllQuotation();
        return ResponseEntity.ok(quotations);
    }

    @PutMapping("{id}")
    public ResponseEntity updateQuotation(@Valid @RequestBody QuotationRequest quotationRequest, @PathVariable long id) {
        Quotation newQuotation = quotationService.updateQuotation(quotationRequest, id);
        return ResponseEntity.ok(newQuotation);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteQuotation(@PathVariable long id) {
        Quotation quotation = quotationService.deleteQuotation(id);
        return ResponseEntity.ok(quotation);
    }
}
