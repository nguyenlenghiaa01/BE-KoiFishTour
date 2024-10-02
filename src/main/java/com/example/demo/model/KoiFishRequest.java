package com.example.demo.model;

import com.example.demo.entity.Breed;
import com.example.demo.entity.Farm;
import com.example.demo.entity.OrderCart;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Random;
import java.util.Set;

public class KoiFishRequest {
    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String name;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    Breed breed;

    @ManyToMany(mappedBy = "koiFishes")
    Set<OrderCart> orderCarts;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    Farm farm;
}
