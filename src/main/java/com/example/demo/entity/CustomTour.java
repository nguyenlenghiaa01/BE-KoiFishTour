package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class CustomTour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    private Date createAt;

    private String status;


    private String duration;
    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;
    @NotBlank(message = "Address cannot be blank")
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double budget;

    private int adult;
    private int child;
    private int infant;

    private boolean isDeleted = false;

    @OneToOne(mappedBy = "customTour")
    private CustomBooking customBooking;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "cus_farm",
            joinColumns = @JoinColumn(name = "customTour_id"),
            inverseJoinColumns = @JoinColumn(name = "farm_id")
    )
    private Set<Farm> farms = new HashSet<>();
}
