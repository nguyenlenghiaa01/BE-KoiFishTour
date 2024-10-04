package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Quotation;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuotationService {
    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    BookingRepository bookingRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Quotation createQuotation(QuotationRequest quotationRequest) {
        Quotation quotation = modelMapper.map(quotationRequest, Quotation.class);
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));

        quotation.setBooking(booking);

        try {
            Quotation newQuotation = quotationRepository.save(quotation);
            return  newQuotation;

        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate quotation id!");
        }
    }

    public List<Quotation> getAllQuotation() {
        List<Quotation> quotations = quotationRepository.findQuotationsByIsDeletedFalse();
        return  quotations;
    }

    public Quotation updateQuotation(QuotationRequest quotationRequest, long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));
        quotation.setBooking(booking);

        return quotationRepository.save(quotation);
    }
    public Quotation deleteQuotation(long id) {
        Quotation oldQuotation = quotationRepository.findQuotationById(id);
        if(oldQuotation == null) {
            throw new NotFoundException("Quotation not found!");
        }

        oldQuotation.setDeleted(true);
        return quotationRepository.save(oldQuotation);
    }
}
