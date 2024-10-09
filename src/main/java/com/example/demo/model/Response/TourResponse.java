package com.example.demo.model.Response;

import com.example.demo.entity.Tour;
import lombok.Data;
import java.util.List;

@Data
public class TourResponse {
    private List<Tour> listTour;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
