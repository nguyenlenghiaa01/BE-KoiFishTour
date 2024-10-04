package com.example.demo.model.Request;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Farm;
import com.example.demo.entity.OrderCart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Random;
import java.util.Set;

@Data
public class KoiFishRequest {
    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String name;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String description;

    private String image;

    long breedId;
    long farmId;

}