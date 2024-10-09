package com.example.demo.model.Response;

import com.example.demo.entity.Farm;
import lombok.Data;

import java.util.List;

@Data
public class FarmResponse {
    private List<Farm> listFarm;
    private int pageNumber;
    private long totalElements;
    private int totalPages;

}
