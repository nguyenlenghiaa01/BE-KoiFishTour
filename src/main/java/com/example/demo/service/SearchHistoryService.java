package com.example.demo.service;

import com.example.demo.entity.HistoryTourSearch;
import com.example.demo.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryService {

    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    public void saveSearchHistory(HistoryTourSearch historyTourSearch) {
        searchHistoryRepository.save(historyTourSearch);
    }
    public List<HistoryTourSearch> getSearchHistoryByUserId(long userId) {
        return searchHistoryRepository.findById(userId);
    }
}

