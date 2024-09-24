package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class QuotationProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long processId;

    @ManyToOne
    @JoinColumn(name = "quotation_id", nullable = false) // Thêm tên cột và yêu cầu không null
    private Quotation quotation; // Sử dụng thực thể Quotation

    @Column(name = "person_id", nullable = false) // Thêm tên cột và yêu cầu không null
    private String personID;

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    @Column(nullable = false) // Yêu cầu không null
    private String status;

    private String note;

    public QuotationProcess() {
        this.createdAt = LocalDateTime.now(); // Gán thời gian hiện tại khi tạo mới
    }
}
