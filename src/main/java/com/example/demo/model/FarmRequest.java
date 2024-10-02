package com.example.demo.model;

import com.example.demo.entity.KoiFish;
import com.example.demo.entity.Tour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;

public class FarmRequest {
    @Column(nullable = false)

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

    @ManyToMany(mappedBy = "farms")
    Set<Tour> tours;

    @OneToMany(mappedBy = "farm")
    @JsonIgnore
    List<KoiFish> koiFishes;
}