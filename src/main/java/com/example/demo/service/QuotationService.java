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
import com.example.demo.model.Response.QuotationResponse;
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
        quotation.setPerAdultPrice(quotationRequest.getPerAdultPrice());
        quotation.setPerChildPrice(quotationRequest.getPerChildPrice());
        return quotationRepository.save(quotation);
    }

    public DataResponse<QuotationResponse> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponse> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {

                QuotationResponse quotationResponse = new QuotationResponse();
                quotationResponse.setAdultPrice(quotation.getBooking().getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(quotation.getBooking().getChild() *quotation.getPerChildPrice());
                quotationResponse.setBookingId(quotation.getBooking().getId());

                double totalPrice = quotation.getBooking().getAdult() * quotation.getPerAdultPrice()+
                        quotation.getBooking().getChild() *quotation.getPerChildPrice()
                        +quotation.getBooking().getPrice();
                quotationResponse.setTotalPrice(totalPrice);
                pendingQuotations.add(quotationResponse);
        }

        DataResponse<QuotationResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(pendingQuotations);
        dataResponse.setTotalElements(pendingQuotations.size());
        dataResponse.setPageNumber(quotationPage.getNumber());
        dataResponse.setTotalPages(quotationPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<QuotationResponse> getAllQuotation(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponse> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
            if (quotation.getStatus() == QuotationEnum.PENDING) {
                QuotationResponse quotationResponse = new QuotationResponse();
                quotationResponse.setAdultPrice(quotation.getBooking().getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(quotation.getBooking().getChild() *quotation.getPerChildPrice());
                quotationResponse.setBookingId(quotation.getBooking().getId());
                double totalPrice =0;
                totalPrice = quotation.getBooking().getAdult() * quotation.getPerAdultPrice()+
                        quotation.getBooking().getChild() *quotation.getPerChildPrice()
                        +quotation.getBooking().getPrice();
                quotationResponse.setTotalPrice(totalPrice);
                pendingQuotations.add(quotationResponse);
            }
        }

        DataResponse<QuotationResponse> dataResponse = new DataResponse<>();
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
        quotation.setStatus(QuotationEnum.APPROVE);
        quotation.setBooking(booking);
        return quotationRepository.save(quotation);
    }

    public Quotation updateQuotationCancel(QuotationRequest quotationRequest, long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));
        quotation.setStatus(QuotationEnum.CANCEL);
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

