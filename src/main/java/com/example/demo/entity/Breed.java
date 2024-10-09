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
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thêm chiến lược tự động tăng cho khóa chính
    private long id; // Thêm trường id làm khóa chính

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
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("BRE%07d", number); // Định dạng với 7 chữ số
    }

    @OneToMany(mappedBy = "breed")
    @JsonIgnore
    private List<KoiFish> koiFishes;
}
