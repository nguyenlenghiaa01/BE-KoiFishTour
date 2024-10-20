package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class HistoryTourSearch {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private LocalDate startDate;
        private String duration;
        private BigDecimal maxPrice;
        private LocalDateTime searchTime;
        private String farm;
        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        @JsonIgnore
        private Account account;
}
