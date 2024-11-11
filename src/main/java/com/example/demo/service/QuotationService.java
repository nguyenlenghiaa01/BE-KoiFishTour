package com.example.demo.service;

import com.example.demo.Enum.QuotationEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.Quotation1Request;
import com.example.demo.model.Request.QuotationRequest;
import com.example.demo.model.Response.*;
import com.example.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotationService {
    @Autowired
    QuotationRepository quotationRepository;
    @Autowired
    QuotationProcessRepository quotationProcessRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomBookingRepository customBookingRepository;
    @Autowired
    CustomTourRepository customTourRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public Quotation createQuotation(QuotationRequest quotationRequest) {
        Quotation quotation = new Quotation();
        CustomBooking booking = customBookingRepository.findById(quotationRequest.getCustomBookingId())
                .orElseThrow(() -> new NotFoundException("Custom Booking not exist!"));
        Quotation quotaions = quotationRepository.findById(quotationRequest.getCustomBookingId())
                .orElseThrow(() -> new NotFoundException("Custom Booking not exist!"));

        Account account = accountRepository.findById(quotationRequest.getSaleId())
                .orElseThrow(() -> new NotFoundException("Sale id not found!"));
        quotation.setCustomBooking(booking);
        quotation.setStatus(QuotationEnum.PENDING);
        quotation.setPerAdultPrice(quotationRequest.getPerAdultPrice());
        quotation.setPerChildPrice(quotationRequest.getPerChildPrice());
        quotation.setAccount(account);
        Quotation savedQuotation = quotationRepository.save(quotation);
        QuotationProcess quotationProcess = new QuotationProcess();

        Account accounts = accountRepository.findById(booking.getAccount().getId())
                .orElseThrow(() -> new NotFoundException("Account not found!"));
        quotationProcess.setCreatedAt(LocalDateTime.now());
        quotationProcess.setStatus(QuotationEnum.PENDING);
        quotationProcess.setNotes("Process created with initial quotation");
        quotationProcess.setQuotation(savedQuotation);
        quotationProcess.setAccount(accounts);

        quotationProcessRepository.save(quotationProcess);
        return savedQuotation;
    }

    public DataResponse<QuotationResponses> getAllQuotationsCancel(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponses> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
            if (quotation.getStatus()!=QuotationEnum.PENDING) {
                QuotationResponses quotationResponse = new QuotationResponses();
                CustomTour customTour = customTourRepository
                        .findById(quotation.getCustomBooking().getCustomTour().getId())
                        .orElseThrow(() -> new NotFoundException("Custom Tour not found!"));
                quotationResponse.setAdultPrice(customTour.getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(customTour.getChild() *quotation.getPerChildPrice());
                quotationResponse.setCustomBookingId(customTour.getCustomBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());
                quotationResponse.setStatus(quotation.getStatus());

                double totalPrice = customTour.getAdult() * quotation.getPerAdultPrice()+
                        customTour.getChild() *quotation.getPerChildPrice()
                        +customTour.getCustomBooking().getPrice();
                quotationResponse.setTotalPrice(totalPrice);
                pendingQuotations.add(quotationResponse);
            }
        }

        DataResponse<QuotationResponses> dataResponse = new DataResponse<>();
        dataResponse.setListData(pendingQuotations);
        dataResponse.setTotalElements(pendingQuotations.size());
        dataResponse.setPageNumber(quotationPage.getNumber());
        dataResponse.setTotalPages(quotationPage.getTotalPages());

        return dataResponse;
    }
    public QuotationResponse getQuotationByBookingId(String  id) {
        CustomBooking customBooking = customBookingRepository.findCustomBookingByCustomBookingId(id);
        if (customBooking == null) {
            throw new NotFoundException("Not found booking code");
        }

        Quotation quotation = quotationRepository
                .findById(customBooking.getQuotation().getId())
                .orElseThrow(() -> new NotFoundException("Quotation not found!"));
        CustomTour customTour = customTourRepository
                .findById(quotation.getCustomBooking().getCustomTour().getId())
                .orElseThrow(() -> new NotFoundException("Custom Tour not found!"));

        QuotationResponse quotationResponse = new QuotationResponse();
        if (!quotation.getStatus().equals(QuotationEnum.CANCEL)) {
            quotationResponse.setQuotationId(quotation.getQuotationId());
            quotationResponse.setCustomBookingId(customTour.getCustomBooking().getId());
            quotationResponse.setAdultPrice(quotation.getPerAdultPrice() * customTour.getAdult());
            quotationResponse.setChildPrice(quotation.getPerChildPrice() * customTour.getChild());
            quotationResponse.setStatus(quotation.getStatus());
            quotationResponse.setCreateAt(quotation.getCreateAt());
            quotationResponse.setSaleName(quotation.getAccount().getFullName());
            quotationResponse.setFullName(customTour.getFullName());
            quotationResponse.setPhone(customTour.getPhone());
            quotationResponse.setEmail(customTour.getEmail());
            quotationResponse.setPriceTour(customTour.getCustomBooking().getPrice());
            quotationResponse.setTotalPrice(quotation.getPerAdultPrice() * customTour.getAdult()
                    + quotation.getPerChildPrice() * customTour.getChild()
                    + customTour.getCustomBooking().getPrice());
        }
        return quotationResponse;
    }


    public DataResponse<QuotationResponses> getAllQuotations(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponses> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
            if (quotation.getStatus()==QuotationEnum.PENDING) {
                QuotationResponses quotationResponse = new QuotationResponses();
                CustomTour customTour = customTourRepository
                        .findById(quotation.getCustomBooking().getCustomTour().getId())
                        .orElseThrow(() -> new NotFoundException("Custom Tour not found!"));

                quotationResponse.setAdultPrice(customTour.getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(customTour.getChild() *quotation.getPerChildPrice());
                quotationResponse.setCustomBookingId(customTour.getCustomBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());

                double totalPrice = customTour.getAdult() * quotation.getPerAdultPrice()+
                        customTour.getChild() *quotation.getPerChildPrice()
                        +customTour.getCustomBooking().getPrice();
                quotationResponse.setTotalPrice(totalPrice);
                pendingQuotations.add(quotationResponse);
            }
        }

        DataResponse<QuotationResponses> dataResponse = new DataResponse<>();
        dataResponse.setListData(pendingQuotations);
        dataResponse.setTotalElements(pendingQuotations.size());
        dataResponse.setPageNumber(quotationPage.getNumber());
        dataResponse.setTotalPages(quotationPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<QuotationResponses> getAllQuotation(@RequestParam int page, @RequestParam int size) {
        Page<Quotation> quotationPage = quotationRepository.findAll(PageRequest.of(page, size));
        List<Quotation> quotations = quotationPage.getContent();
        List<QuotationResponses> pendingQuotations = new ArrayList<>();
        for (Quotation quotation : quotations) {
                QuotationResponses quotationResponse = new QuotationResponses();
            CustomTour customTour = customTourRepository
                    .findById(quotation.getCustomBooking().getCustomTour().getId())
                    .orElseThrow(() -> new NotFoundException("Custom Tour not found!"));

            quotationResponse.setAdultPrice(customTour.getAdult() * quotation.getPerAdultPrice());
                quotationResponse.setChildPrice(customTour.getChild() *quotation.getPerChildPrice());
                quotationResponse.setCustomBookingId(customTour.getCustomBooking().getId());
                quotationResponse.setQuotationId(quotation.getId());

                double totalPrice = customTour.getAdult() * quotation.getPerAdultPrice()+
                        customTour.getChild() *quotation.getPerChildPrice()
                        +customTour.getCustomBooking().getPrice();
                quotationResponse.setTotalPrice(totalPrice);
                pendingQuotations.add(quotationResponse);
        }

        DataResponse<QuotationResponses> dataResponse = new DataResponse<>();
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
        CustomBooking customBooking = customBookingRepository.findById(quotationRequest.getCustomBookingId()).
                orElseThrow(() -> new NotFoundException("Custom Booking not exist!"));
        quotation.setStatus(QuotationEnum.APPROVE);
        quotation.setCustomBooking(customBooking);
        return quotationRepository.save(quotation);
    }
    public Quotation setQuotationCancel(Quotation1Request quotation1Request,String id) {
        Quotation quotation = quotationRepository.findQuotationByQuotationId(id);

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        CustomBooking customBooking = customBookingRepository
                .findById(quotation.getCustomBooking().getId())
                .orElseThrow(() -> new NotFoundException("Booking not found!"));
        customBooking.setStatus("CANCEL");
        customBookingRepository.save(customBooking);
        quotation.setStatus(QuotationEnum.CANCEL);
        Quotation quotation1=quotationRepository.save(quotation);
        QuotationProcess quotationProcess = new QuotationProcess();
        quotationProcess.setAccount(quotation1.getCustomBooking().getAccount());
        quotationProcess.setQuotation(quotation1);
        quotationProcess.setCreatedAt(LocalDateTime.now());
        quotationProcess.setNotes(quotation1Request.getNote());

        quotationProcessRepository.save(quotationProcess);
        return quotation1;
    }
    public Quotation setQuotationApprove(Quotation1Request quotation1Request,long id) {
        Quotation quotation = quotationRepository.findQuotationById(id);
        CustomBooking customBooking = customBookingRepository
                .findById(quotation.getCustomBooking().getId())
                .orElseThrow(() -> new NotFoundException("Booking not found!"));

        if(quotation == null) {
            throw new NotFoundException("Quotation not found!");
        }
        quotation.setStatus(QuotationEnum.APPROVE);
        customBooking.setStatus("APPROVE");
        customBookingRepository.save(customBooking);
         Quotation quotation1=quotationRepository.save(quotation);
        QuotationProcess quotationProcess = new QuotationProcess();
        quotationProcess.setAccount(quotation1.getCustomBooking().getAccount());
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
        CustomBooking customBooking = customBookingRepository
                .findById(quotationRequest.getCustomBookingId()).
                orElseThrow(() -> new NotFoundException("Booking not exist!"));
        quotation.setStatus(QuotationEnum.CANCEL);
        quotation.setCustomBooking(customBooking);

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

