package com.example.demo.api;

import com.example.demo.entity.Report;
import com.example.demo.entity.Tour;
import com.example.demo.model.Request.ReportRequest;
import com.example.demo.model.Request.TourRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.service.ReportService;
import com.example.demo.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ReportAPI {
    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReportRequest reportRequest) {
        Report report = reportService.createNewReport(reportRequest);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<?> get(@RequestParam int page, @RequestParam int size){
        DataResponse dataResponse = reportService.getAllReports(page, size);
        return ResponseEntity.ok(dataResponse);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Report report = reportService.deleteReport(id);
        return ResponseEntity.ok(report);
    }
}

