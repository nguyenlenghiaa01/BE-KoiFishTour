package com.example.demo.api;


import com.example.demo.entity.Feedback;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedBackAPI {
    @Autowired
    FeedbackService feedbackService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        Feedback newFeedBack = feedbackService.createNewFeedback(feedbackRequest);
        //return ve font end
        return ResponseEntity.ok(newFeedBack);
    }

    // Get danh sách breed
    @GetMapping
    public ResponseEntity get(){
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbacks);
    }
    // /api/feedback/{id} => id cua thang feedback minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateFeedback(@Valid @RequestBody FeedbackRequest feedback, @PathVariable long id){//valid kich hoat co che vadilation
        Feedback newFeedback = feedbackService.updateFeedback(feedback,id);
        return ResponseEntity.ok(newFeedback);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteFeedback(@PathVariable long id){
        Feedback newFeedback = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(newFeedback);
    }
}
