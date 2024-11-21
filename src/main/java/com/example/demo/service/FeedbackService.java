package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Feedback;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.FeedbackResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AccountRepository accountRepository;

    public Feedback createNewFeedback(FeedbackRequest feedbackRequest) {
        Feedback feedback = new Feedback();
        Booking booking = bookingRepository.findBookingByBookingId(feedbackRequest.getBookingId());
        if(booking == null) {
            throw new NotFoundException("Booking not found!");
        }
        Account customer = accountRepository.findAccountById(feedbackRequest.getAccountId());
        if(customer == null) {
            throw new NotFoundException("Customer not found!");
        }

        feedback.setComment(feedbackRequest.getComment());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCustomer(customer);
        feedback.setBooking(booking);
        try {
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate Feedback id!");
        }
    }


    public DataResponse<FeedbackResponse> getAllFeedback(@RequestParam int page, @RequestParam int size) {
        Page<Feedback> feedbackPage = feedbackRepository.findAll(PageRequest.of(page, size));

        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

        for (Feedback feedback : feedbackPage.getContent()) {
            FeedbackResponse response = new FeedbackResponse();
            response.setId(feedback.getId());
            response.setComment(feedback.getComment());
            response.setRating(feedback.getRating());
            response.setEmail(feedback.getCustomer().getEmail());
            response.setImage(feedback.getCustomer().getImage());
            response.setNameCustomer(feedback.getCustomer().getFullName());
            if (feedback.getRating() >= 4) {
                feedbackResponses.add(response);
            }
        }
        DataResponse<FeedbackResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(feedbackResponses);
        dataResponse.setTotalElements(feedbackPage.getTotalElements());
        dataResponse.setTotalPages(feedbackPage.getTotalPages());
        dataResponse.setPageNumber(feedbackPage.getNumber());

        return dataResponse;
    }

    public Feedback getFeedBackByBookingId(String bookingId) {
        Feedback feedback = feedbackRepository.findFeedbackByBooking_BookingId(bookingId);
        return feedback;
    }

    public DataResponse<FeedbackResponse> getFeedBack(@RequestParam int page,@RequestParam int size) {
        Page<FeedbackResponse> feedbackResponsePage = feedbackRepository.findAllFeedbackResponses(PageRequest.of(page, size));
        List<FeedbackResponse> feedbacks = feedbackResponsePage.getContent();
        DataResponse<FeedbackResponse> response = new DataResponse<>();
        response.setListData(feedbacks);
        response.setTotalElements(feedbackResponsePage.getTotalElements());
        response.setTotalPages(feedbackResponsePage.getTotalPages());
        response.setPageNumber(feedbackResponsePage.getNumber());
        return response;
    }


    public Feedback updateFeedback(FeedbackRequest feedback, long id){

        Feedback oldFeedback = feedbackRepository.findFeedbackById(id);
        if(oldFeedback ==null){
            throw new NotFoundException("FeedBack not found !");
        }
        oldFeedback.setRating(feedback.getRating());
        oldFeedback.setComment(feedback.getComment());
        return feedbackRepository.save(oldFeedback);
    }
    public Feedback deleteFeedback(long id){
        Feedback oldFeedback = feedbackRepository.findFeedbackById(id);
        if(oldFeedback ==null){
            throw new NotFoundException("FeedBack not found !");
        }
        oldFeedback.setDeleted(true);
        return feedbackRepository.save(oldFeedback);
    }
}
