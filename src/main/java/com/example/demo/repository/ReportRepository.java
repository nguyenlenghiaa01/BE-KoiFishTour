package com.example.demo.repository;

import com.example.demo.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    Report findReportByReportId(long reportId);

    List<Report> findReportsByIsDeletedFalse();

}
