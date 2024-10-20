package com.example.demo.api;

import com.example.demo.entity.HistoryTourSearch;
import com.example.demo.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search-history")
public class HistorySearchAPI {

   @Autowired
    SearchHistoryService saveSearchHistory;

    @PostMapping
    public void addSearchHistory(@RequestBody HistoryTourSearch historyTourSearch) {
        saveSearchHistory.saveSearchHistory(historyTourSearch);
    }

    @GetMapping("/{userId}")
    public List<HistoryTourSearch> getSearchHistory(@PathVariable long userId) {
        return saveSearchHistory.getSearchHistoryByUserId(userId);
    }
}
