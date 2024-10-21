package com.example.demo.model.Response;

import lombok.Data;

@Data
public class BreedResponse {
    private long id;
    private String breedId;
    private boolean isDeleted;
    private String breedName;
    private String description;
}
