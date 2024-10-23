package com.example.demo.model.Response;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Farm;
import lombok.Data;

@Data
public class KoiFishResponse {
    private long id;
    private String koiCode;
    private String name;
    private String description;
    private boolean isDeleted;
    private String image;
    Breed breed;
    Farm farm;

}
