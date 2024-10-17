package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@Entity
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "BRE\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String breedId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String breedName;

    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String description;

    @PrePersist
    private void prePersist() {
        this.breedId = generateBreedId();
    }

    private String generateBreedId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("BRE%07d", number);
    }

    @OneToMany(mappedBy = "breed")
    @JsonIgnore
    private List<KoiFish> koiFishes;

    @ManyToMany(mappedBy = "breeds")
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();
}
