package com.example.demo.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponse {

    private long id;
    private String comment;
    private int rating;
    private String email;
}
