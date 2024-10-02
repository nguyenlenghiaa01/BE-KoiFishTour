package com.example.demo.model;

import com.example.demo.entity.Account;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class FeedbackRequest {
    private String comment;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;
}