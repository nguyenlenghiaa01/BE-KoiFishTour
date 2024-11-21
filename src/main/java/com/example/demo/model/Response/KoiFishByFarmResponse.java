package com.example.demo.model.Response;

import com.example.demo.entity.KoiFish;
import lombok.Data;

import java.util.List;

@Data
public class KoiFishByFarmResponse {
    private String farmName;
    private List<KoiFish> koiFishList;
}
