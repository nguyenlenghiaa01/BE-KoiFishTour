package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class FeedbackRequest {
    private String comment;

    private int rating;

    long accountId;
}
