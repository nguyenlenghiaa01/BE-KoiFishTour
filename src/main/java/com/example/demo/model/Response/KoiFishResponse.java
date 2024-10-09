package com.example.demo.model.Response;

import com.example.demo.entity.KoiFish;
import lombok.Data;

import java.util.List;

@Data
public class KoiFishResponse {
    private List<KoiFish> listFish;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
