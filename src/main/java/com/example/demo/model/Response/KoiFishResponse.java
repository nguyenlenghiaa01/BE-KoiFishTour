package com.example.demo.model.Response;

import com.example.demo.entity.KoiFish;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class KoiFishResponse {
    private List<KoiFish> listFish;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
}
