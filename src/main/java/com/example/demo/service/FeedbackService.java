package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.FeedbackRequest;
import com.example.demo.repository.AccountRepository;
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

    public Feedback createNewFeedback(FeedbackRequest feedbackRequest){
        //add feedback vao database bang repsitory
        Feedback feedback = modelMapper.map(feedbackRequest, Feedback.class);

        Account account = accountRepository.findById(feedbackRequest.getAccountId()).orElseThrow(() -> new NotFoundException("Account not exist!"));

        feedback.setAccount(account);
        try {
            Feedback newFeedback = feedbackRepository.save(feedback);
            return newFeedback;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate FeedBack id !");
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
