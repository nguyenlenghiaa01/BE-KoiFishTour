package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.Set;

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

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String name;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public KoiFish() {
        this.koiId = generateKoiId();
    }

    private String generateKoiId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("KOI%07d", number); // Định dạng với 7 chữ số
    }

    @ManyToOne
    @JoinColumn(name = "breed_id")
    Breed breed;

    @ManyToMany(mappedBy = "koiFishes")
    Set<OrderCart> orderCarts;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    Farm farm;
}
