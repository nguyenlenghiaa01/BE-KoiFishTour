package com.example.demo.service;

import com.example.demo.Enum.QuotationEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.Quotation1Request;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.model.Response.QuotationProcessResponse;
import com.example.demo.model.Response.QuotationResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.QuotationProcessRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuotationService {
    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    QuotationProcessRepository quotationProcessRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AccountRepository accountRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Quotation createQuotation(QuotationRequest quotationRequest) {
        Quotation quotation = new Quotation();
        Booking booking = bookingRepository.findById(quotationRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException("Booking not exist!"));

        Account account = accountRepository.findById(quotationRequest.getSaleId())
                .orElseThrow(() -> new NotFoundException("Sale id not found!"));
        quotation.setBooking(booking);
        quotation.setStatus(QuotationEnum.PENDING);
        quotation.setPerAdultPrice(quotationRequest.getPerAdultPrice());
        quotation.setPerChildPrice(quotationRequest.getPerChildPrice());
        quotation.setAccount(account);
        return quotationRepository.save(quotation);
    }

    public DataResponse<QuotationResponse> getAllQuotationsCancel(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponse> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
            if (quotation.getStatus()!=QuotationEnum.PENDING) {
                QuotationResponse quotationResponse = new QuotationResponse();
                quotationResponse.setAdultPrice(quotation.getBooking().getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(quotation.getBooking().getChild() *quotation.getPerChildPrice());
                quotationResponse.setBookingId(quotation.getBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());
                quotationResponse.setStatus(quotation.getStatus());

                double totalPrice = quotation.getBooking().getAdult() * quotation.getPerAdultPrice()+
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
    public QuotationResponse getQuotationByBookingId(String id) {
        Booking booking = bookingRepository.findBookingsByBookingId(id);
        if (booking == null) {
            throw new NotFoundException("Not found booking code");
        }

        Quotation quotation = quotationRepository.findById(booking.getId()).orElseThrow(() -> new NotFoundException("Quotation not found!"));

        QuotationResponse quotationResponse = new QuotationResponse();
        quotationResponse.setQuotationId(quotation.getId());
        quotationResponse.setBookingId(booking.getId());
        quotationResponse.setAdultPrice(quotation.getPerAdultPrice()*booking.getAdult());
        quotationResponse.setChildPrice(quotation.getPerChildPrice()*booking.getChild());
        quotationResponse.setStatus(quotation.getStatus());
        quotationResponse.setCreateAt(quotation.getCreateAt());
        quotationResponse.setSaleName(quotation.getAccount().getFullName());
        quotationResponse.setFullName(booking.getFullName());
        quotationResponse.setPhone(booking.getPhone());
        quotationResponse.setEmail(booking.getEmail());
        quotationResponse.setPriceTour(booking.getPrice());
        quotationResponse.setTotalPrice(quotation.getPerAdultPrice()*booking.getAdult()
                                        +quotation.getPerChildPrice()*booking.getChild()
                                        +booking.getPrice());

        return quotationResponse;
    }


    public DataResponse<QuotationResponse> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponse> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
            if (quotation.getStatus()==QuotationEnum.PENDING) {
                QuotationResponse quotationResponse = new QuotationResponse();
                quotationResponse.setAdultPrice(quotation.getBooking().getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(quotation.getBooking().getChild() *quotation.getPerChildPrice());
                quotationResponse.setBookingId(quotation.getBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());

                double totalPrice = quotation.getBooking().getAdult() * quotation.getPerAdultPrice()+
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

    public DataResponse<QuotationResponse> getAllQuotation(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponse> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
                QuotationResponse quotationResponse = new QuotationResponse();
                quotationResponse.setAdultPrice(quotation.getBooking().getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(quotation.getBooking().getChild() *quotation.getPerChildPrice());
                quotationResponse.setBookingId(quotation.getBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());

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
    public Quotation setQuotationCancel(Quotation1Request quotation1Request,long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        quotation.setStatus(QuotationEnum.CANCEL);
        Quotation quotation1=quotationRepository.save(quotation);
        QuotationProcess quotationProcess = new QuotationProcess();
        quotationProcess.setAccount(quotation1.getBooking().getAccount());
        quotationProcess.setQuotation(quotation1);
        quotationProcess.setCreatedAt(LocalDateTime.now());
        quotationProcess.setNotes(quotation1Request.getNote());
//        quotationProcess.setStatus(quotation1.getStatus());

        quotationProcessRepository.save(quotationProcess);
        return quotation1;
    }
    public Quotation setQuotationApprove(Quotation1Request quotation1Request,long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        quotation.setStatus(QuotationEnum.APPROVE);
         Quotation quotation1=quotationRepository.save(quotation);
        QuotationProcess quotationProcess = new QuotationProcess();
        quotationProcess.setAccount(quotation1.getBooking().getAccount());
        quotationProcess.setQuotation(quotation1);
        quotationProcess.setCreatedAt(LocalDateTime.now());
        quotationProcess.setNotes(quotation1Request.getNote());

         quotationProcessRepository.save(quotationProcess);
         return quotation1;
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

