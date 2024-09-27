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
    private long id;

    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    @Column(nullable = false) // Yêu cầu không null
    private String status;

    private String notes;

    public QuotationProcess() {
        this.createdAt = LocalDateTime.now(); // Gán thời gian hiện tại khi tạo mới
    }

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}
