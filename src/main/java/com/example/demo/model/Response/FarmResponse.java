package com.example.demo.model.Response;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Farm;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.Tour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Data
public class FarmResponse {
    private long id;
    private String farmId;
    private boolean isDeleted;
    private String farmName;
    private String location;
    private String owner;
    private List<String> image;

}
