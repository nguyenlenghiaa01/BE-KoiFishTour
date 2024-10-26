package com.example.demo.service;

import com.example.demo.Enum.QuotationEnum;
import com.example.demo.entity.Booking;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.Quotation;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotationService {
    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    BookingRepository bookingRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Quotation createQuotation(QuotationRequest quotationRequest) {
        Quotation quotation = new Quotation();
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));

        quotation.setBooking(booking);
        quotation.setStatus(QuotationEnum.PENDING);

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

    public DataResponse<Quotation> getAllQuotation(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<Quotation> pendingQuotations = new ArrayList<>();

        for (Quotation quotation : quotations) {
            if (quotation.getStatus() == QuotationEnum.PENDING) {
                pendingQuotations.add(quotation);
            }
        }

        DataResponse<Quotation> dataResponse = new DataResponse<>();
        dataResponse.setListData(pendingQuotations);
        dataResponse.setTotalElements(pendingQuotations.size());
        dataResponse.setPageNumber(quotationPage.getNumber());
        dataResponse.setTotalPages(quotationPage.getTotalPages());

        return dataResponse;
    }




    public Quotation updateQuotation(QuotationRequest quotationRequest, long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));
        quotation.setBooking(booking);
        quotation.setStatus(quotationRequest.getStatus());

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

