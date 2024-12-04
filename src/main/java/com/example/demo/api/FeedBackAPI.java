package com.example.demo.api;


import com.example.demo.entity.Feedback;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FeedbackResponse;
import com.example.demo.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedBackAPI {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Feedback> create(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        Feedback newFeedback = feedbackService.createNewFeedback(feedbackRequest);
        simpMessagingTemplate.convertAndSend("topic/feedback","CREATE NEW FEEDBACK");
        return ResponseEntity.ok(newFeedback);
    }

    @PreAuthorize("hasAuthority('ADMIN')")

    @GetMapping("/get")
    public ResponseEntity<DataResponse<FeedbackResponse>> get(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<FeedbackResponse> feedbackResponse = feedbackService.getFeedBack(page, size);
        return ResponseEntity.ok(feedbackResponse);
    }


    @GetMapping("/guest/get")
    public ResponseEntity<DataResponse<FeedbackResponse>> getAll(
            @RequestParam int page,
            @RequestParam int size) {
        DataResponse<FeedbackResponse> feedbackResponse = feedbackService.getAllFeedback(page, size);
        return ResponseEntity.ok(feedbackResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")

    @GetMapping("/bookingId")
    public ResponseEntity<Feedback> getFeedBackByBookingId(@RequestParam String bookingId) {
        Feedback feedback = feedbackService.getFeedBackByBookingId(bookingId);
        return ResponseEntity.ok(feedback);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("{id}")
    public ResponseEntity<Feedback> updateFeedback(@Valid @RequestBody FeedbackRequest feedback, @PathVariable long id) {
            Feedback updatedFeedback = feedbackService.updateFeedback(feedback, id);
            simpMessagingTemplate.convertAndSend("topic/feedback","UPDATE FEEDBACK");
            return ResponseEntity.ok(updatedFeedback);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Feedback> deleteFeedback(@PathVariable long id) {
            Feedback deletedFeedback = feedbackService.deleteFeedback(id);
        simpMessagingTemplate.convertAndSend("topic/feedback","DELETE FEEDBACK");
            return ResponseEntity.ok(deletedFeedback);

    }
}
