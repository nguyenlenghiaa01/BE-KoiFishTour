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
public class Farm {
    @Id // đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "FAR\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String farmId;

    public String generateFarmId() {
        Random random = new Random();
        int number = random.nextInt(10000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("FAR%07d", number); // Định dạng với 7 chữ số
    }

    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name must not contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character must not be a space!")
    private String farmName;

    @Pattern(regexp = "^[^\\s].*", message = "First character must not be a space!")
    private String location;

    @NotBlank(message = "Owner name can not be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Owner name must not contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character must not be a space!")
    private String owner;

    private String image;

    @PrePersist
    private void prePersist() {
        this.farmId = generateFarmId();
    }


    @ManyToMany(mappedBy = "farms")
    @JsonIgnore
    private Set<Tour> tours = new HashSet<>();

    @OneToMany(mappedBy = "farm")
    @JsonIgnore
    List<KoiFish> koiFishes;
}
