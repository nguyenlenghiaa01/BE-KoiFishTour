package com.example.demo.model;

import com.example.demo.entity.KoiFish;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
@Data
public class BreedRequest {
    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String breedName;

    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String description;

    @OneToMany(mappedBy = "breed")
    @JsonIgnore
    List<KoiFish> koiFishes;
}
