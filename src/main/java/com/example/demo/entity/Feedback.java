package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Comment can not be blank")
    private String comment;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

}
