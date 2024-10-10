package com.example.demo.api;


import com.example.demo.entity.Feedback;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedBackAPI {

    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        Feedback newFeedback = feedbackService.createNewFeedback(feedbackRequest);
        return ResponseEntity.ok(newFeedback);
    }

    // Lấy feedback
    @GetMapping("/guest/get")
    public ResponseEntity<List<Feedback>> get() {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbacks);
    }

    // update feedback
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateFeedback(@Valid @RequestBody FeedbackRequest feedback, @PathVariable long id) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(feedback, id);
            return ResponseEntity.ok(updatedFeedback);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Xóa feedback
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable long id) {
            Feedback deletedFeedback = feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(deletedFeedback);

    }
}
