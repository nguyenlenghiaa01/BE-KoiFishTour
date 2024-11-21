package com.example.demo.repository;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Quotation;
import com.example.demo.model.Response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
    Feedback findFeedbackById(long id);
    List<Feedback> findFeedbacksByIsDeletedFalse();

    @Query("SELECT new com.example.demo.model.Response.FeedbackResponse(f.id, f.comment, f.rating, c.email,c.image,c.fullName) " +
            "FROM Feedback f JOIN f.customer c WHERE f.isDeleted = false")
    Page<FeedbackResponse> findAllFeedbackResponses(Pageable pageable);

    Page<Feedback> findAll(Pageable pageable);

    Feedback findFeedbackByBooking_BookingId(String bookingId);


}
