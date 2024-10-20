package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.ReportRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.ReportResponse;
import com.example.demo.model.Response.TourResponse;
import com.example.demo.repository.ReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    AuthenticationService authenticationService;

    public Report createNewReport(ReportRequest reportRequest) {
        Report report = new Report();
        Account currentAccount = authenticationService.getCurrentAccount();
        report.setCreatedBy(currentAccount.getUsername());
        report.setCreatedDate(LocalDateTime.now());
        report.setReportContent(reportRequest.getReportContent());
        report.setReportType(reportRequest.getReportType());

        try {
            Report newReport = reportRepository.save(report);
            return newReport;
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate report ID!");
        }
    }

    public DataResponse<ReportResponse> getAllReports(@RequestParam int page, @RequestParam int size) {
        Page reportPage = reportRepository.findAll(PageRequest.of(page, size));
        List<Report> reports = reportPage.getContent();
        List<ReportResponse> reportResponses = new ArrayList<>();

        for(Report report: reports) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setReportId(report.getReportId());
            reportResponse.setCreatedBy(report.getCreatedBy());
            reportResponse.setReportType(report.getReportType());
            reportResponse.setReportContent(report.getReportContent());
            reportResponse.setCreatedDate(report.getCreatedDate());

            reportResponses.add(reportResponse);
        }
        DataResponse<ReportResponse> dataResponse = new DataResponse<ReportResponse>();
        dataResponse.setListData(reportResponses);
        dataResponse.setTotalElements(reportPage.getTotalElements());
        dataResponse.setPageNumber(reportPage.getNumber());
        dataResponse.setTotalPages(reportPage.getTotalPages());
        return dataResponse;
    }

    public Report deleteReport(long id) {
        Report oldReport = reportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report not found!"));
        oldReport.setDeleted(true);
        return reportRepository.save(oldReport);
    }
}
