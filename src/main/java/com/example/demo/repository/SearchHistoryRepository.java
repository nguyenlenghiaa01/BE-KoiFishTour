package com.example.demo.repository;

import com.example.demo.entity.HistoryTourSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<HistoryTourSearch, Long> {
    List<HistoryTourSearch> findById(long Id);
}
