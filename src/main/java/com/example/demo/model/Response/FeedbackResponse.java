package com.example.demo.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private long id;
    private String comment;
    private int rating;
    private String email;
    private String image;
    private String nameCustomer;
}
