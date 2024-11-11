package com.example.demo.api;

import com.example.demo.entity.Quotation;
import com.example.demo.model.Request.Quotation1Request;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.QuotationResponse;
import com.example.demo.model.Response.QuotationResponses;
import com.example.demo.service.QuotationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/quotation")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class QuotationAPI {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    QuotationService quotationService;

    @PostMapping
    public ResponseEntity<?> createQuotation(@Valid @RequestBody QuotationRequest quotationRequest) {
        Quotation newQuotation = quotationService.createQuotation(quotationRequest);
        return ResponseEntity.ok(newQuotation);
    }

    @GetMapping
    public ResponseEntity<?> getAllQuotation(int page, int size) {
        DataResponse<QuotationResponses> quotations = quotationService.getAllQuotation(page, size);
        return ResponseEntity.ok(quotations);
    }
    @GetMapping("/cancel")
    public ResponseEntity<?> getAllQuotationCancel(int page, int size) {
        DataResponse<QuotationResponses> quotations = quotationService.getAllQuotationsCancel(page, size);
        return ResponseEntity.ok(quotations);
    }
    @GetMapping("/bookingCode")
    public ResponseEntity<?> getQuotationByBookingCode(String id){
        QuotationResponse quotationResponse = quotationService.getQuotationByBookingId(id);
        return ResponseEntity.ok(quotationResponse);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> get(int page , int size){
        DataResponse<QuotationResponses> quotationProcesses = quotationService.getAllQuotations(page, size);
        return ResponseEntity.ok(quotationProcesses);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateQuotation(@Valid @RequestBody QuotationRequest quotationRequest, @PathVariable long id) {
        Quotation newQuotation = quotationService.updateQuotation(quotationRequest, id);
        return ResponseEntity.ok(newQuotation);
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> updateQuotationCancel(@Valid @RequestBody QuotationRequest quotationRequest, @PathVariable long id) {
        Quotation newQuotation = quotationService.updateQuotationCancel(quotationRequest, id);
        return ResponseEntity.ok(newQuotation);
    }
    @PostMapping("/cancel")
    public ResponseEntity<Quotation> postId(Quotation1Request quotation1Request,String id){
        Quotation newQuotation = quotationService.setQuotationCancel(quotation1Request, id);
        return  ResponseEntity.ok(newQuotation);
    }
    @PostMapping("/approve")
    public ResponseEntity<Quotation> post(Quotation1Request quotation1Request,long id){
        Quotation newQuotation = quotationService.setQuotationApprove(quotation1Request, id);
        simpMessagingTemplate.convertAndSend("topic/quotation","APPROVED BOOKING");
        return  ResponseEntity.ok(newQuotation);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteQuotation(@PathVariable long id) {
        Quotation quotation = quotationService.deleteQuotation(id);
        return ResponseEntity.ok(quotation);
    }
}
