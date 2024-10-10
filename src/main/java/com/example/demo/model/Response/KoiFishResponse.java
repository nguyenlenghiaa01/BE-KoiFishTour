package com.example.demo.model.Response;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Farm;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class KoiFishResponse {
    private List<KoiFish> listFish;
    private int pageNumber;
    private long totalElements;
    private int totalPages;

}
