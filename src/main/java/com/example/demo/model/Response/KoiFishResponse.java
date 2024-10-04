package com.example.demo.model.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class KoiFishResponse {
    private  String name;

    private String description;

    private String image;
}
