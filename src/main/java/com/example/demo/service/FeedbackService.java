package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Feedback;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    // xu ly nhung logic lien qua
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    BookingRepository bookingRepository;

    public Feedback createNewFeedback(FeedbackRequest feedbackRequest) {
        // create feedback
        Feedback feedback = new Feedback();
        Booking booking = bookingRepository.findById(feedbackRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking Id not exist!"));

        feedback.setComment(feedbackRequest.getComment());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setCustomer(authenticationService.getCurrentAccount());
        feedback.setBooking(booking);
        try {
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate Feedback id!");
        }
    }

    public List<Feedback> getAllFeedback(){
        // lay tat ca feedback trong DB
        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByIsDeletedFalse();
        return feedbacks;
    }
    public Feedback updateFeedback(FeedbackRequest feedback, long id){

        Feedback oldFeedback = feedbackRepository.findFeedbackById(id);
        if(oldFeedback ==null){
            throw new NotFoundException("FeedBack not found !");//dung viec xu ly ngay tu day
        }
        //=> co feedback co ton tai;
        oldFeedback.setRating(feedback.getRating());
        oldFeedback.setComment(feedback.getComment());
        return feedbackRepository.save(oldFeedback);
    }
    public Feedback deleteFeedback(long id){
        Feedback oldFeedback = feedbackRepository.findFeedbackById(id);
        if(oldFeedback ==null){
            throw new NotFoundException("FeedBack not found !");//dung viec xu ly ngay tu day
        }
        oldFeedback.setDeleted(true);
        return feedbackRepository.save(oldFeedback);
    }
}
