package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
public class KoiFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "KOI\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String koiId;

    @NotBlank(message = "Description can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Description must not have numbers and the first character must not be a space!")
    private String description;

    private boolean isDeleted = false;

    private String image;

    public KoiFish() {
        this.koiId = generateKoiId();
    }

    private String generateKoiId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("KOI%07d", number);
    }

    @ManyToOne
    @JoinColumn(name = "breed_id")
    Breed breed;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    Farm farm;

    @ManyToMany(mappedBy = "koiFishes")
    @JsonIgnore
    private List<KoiFishOrder> koiFishOrders;
}