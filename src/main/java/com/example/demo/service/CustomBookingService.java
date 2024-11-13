package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.CustomBookingRequest;
import com.example.demo.model.Request.CustomBookingRequests;
import com.example.demo.model.Response.CustomBookingResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CustomBookingService {
    @Autowired
    QuotationRepository quotationRepository;
    @Autowired
    CustomBookingRepository customBookingRepository;
    @Autowired
    CustomTourRepository customTourRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    FarmRepository farmRepository;

    public CustomBooking createNewCusBooking(CustomBookingRequest customBookingRequest){
        CustomTour customTour = customTourRepository.findById(customBookingRequest.getCustomTourId()).orElseThrow(() -> new NotFoundException("Custom tour id not found!"));
        Account sale = authenticationService.getCurrentAccount();
        Account consulting = accountRepository.findAccountById(customBookingRequest.getConsultingId());
        if(consulting == null){
            throw  new NotFoundException("Consulting not found");
        }

        CustomBooking customBooking= new CustomBooking();
        customBooking.setAccount(sale);
        customBooking.setConsulting(consulting);
        customBooking.setPrice(customBookingRequest.getPrice());
        customBooking.setStatus("PENDING");
        customBooking.setCreateAt(new Date());
        return customBookingRepository.save(customBooking);
    }

    public DataResponse<CustomBookingResponse> getAllCusBooking(@RequestParam int page, @RequestParam int size){
        Page<CustomBooking> cusPage = customBookingRepository.findAll(PageRequest.of(page, size));
        List<CustomBooking> customTours = cusPage.getContent();
        List<CustomBookingResponse> customTourResponses = new ArrayList<>();

        for(CustomBooking customBooking : customTours) {
            CustomBookingResponse customTourResponse = new CustomBookingResponse();
            customTourResponse.setId(customBooking.getId());
            customTourResponse.setPrice(customBooking.getPrice());
            customTourResponse.setAddress(customBooking.getCustomTour().getAddress());
            customTourResponse.setDuration(customBooking.getCustomTour().getDuration());
            customTourResponse.setStartDate(customBooking.getCustomTour().getStartDate());
            customTourResponse.setEmail(customBooking.getCustomTour().getEmail());
            customTourResponse.setPhone(customBooking.getCustomTour().getPhone());
            customTourResponse.setAdult(customBooking.getCustomTour().getAdult());
            customTourResponse.setChild(customBooking.getCustomTour().getChild());
            customTourResponse.setInfant(customBooking.getCustomTour().getInfant());
            customTourResponse.setFarm(customBooking.getCustomTour().getFarms());
            customTourResponse.setStatus(customBooking.getStatus());
            customTourResponse.setFullName(customTourResponse.getFullName());

            customTourResponses.add(customTourResponse);
        }

        DataResponse<CustomBookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(customTourResponses);
        dataResponse.setTotalElements(cusPage.getTotalElements());
        dataResponse.setPageNumber(cusPage.getNumber());
        dataResponse.setTotalPages(cusPage.getTotalPages());

        return dataResponse;
    }

    public CustomBooking updateCus(CustomBookingRequests customBookingRequests, long id){
        CustomBooking oldCus = customBookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found!"));
        oldCus.setPrice(customBookingRequests.getPrice());
        oldCus.setStatus(customBookingRequests.getStatus());
        return customBookingRepository.save(oldCus);
    }
    public DataResponse<CustomBookingResponse> getAllCusBookingByCustomerId(@RequestParam int page, @RequestParam int size, String id){
        Account customer = accountRepository.findAccountByCode(id);
        if(customer ==null){
            throw  new NotFoundException("Customer not found");
        }
        Page<CustomBooking> cusPage = customBookingRepository.findAll(PageRequest.of(page, size));
        List<CustomBooking> customTours = cusPage.getContent();
        List<CustomBookingResponse> customTourResponses = new ArrayList<>();

        for(CustomBooking customBooking : customTours) {
            CustomBookingResponse customTourResponse = new CustomBookingResponse();
            customTourResponse.setId(customBooking.getId());
            customTourResponse.setCusBookingId(customBooking.getCustomBookingId());
            customTourResponse.setPrice(customBooking.getPrice());
            customTourResponse.setAddress(customBooking.getCustomTour().getAddress());
            customTourResponse.setDuration(customBooking.getCustomTour().getDuration());
            customTourResponse.setStartDate(customBooking.getCustomTour().getStartDate());
            customTourResponse.setEmail(customBooking.getCustomTour().getEmail());
            customTourResponse.setPhone(customBooking.getCustomTour().getPhone());
            customTourResponse.setAdult(customBooking.getCustomTour().getAdult());
            customTourResponse.setChild(customBooking.getCustomTour().getChild());
            customTourResponse.setInfant(customBooking.getCustomTour().getInfant());
            customTourResponse.setFarm(customBooking.getCustomTour().getFarms());
            customTourResponse.setStatus(customBooking.getStatus());
            customTourResponse.setFullName(customer.getFullName());
            customTourResponse.setCustomTour(customBooking.getCustomTour());

            customTourResponses.add(customTourResponse);
        }

        DataResponse<CustomBookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(customTourResponses);
        dataResponse.setTotalElements(cusPage.getTotalElements());
        dataResponse.setPageNumber(cusPage.getNumber());
        dataResponse.setTotalPages(cusPage.getTotalPages());

        return dataResponse;
    }


    public CustomBooking getQuotation(long id){
        Quotation quotation = quotationRepository.findById(id).orElseThrow(() -> new NotFoundException("Quotation not found!"));
        CustomBooking customBooking = customBookingRepository.findById(quotation.getCustomBooking().getId()).orElseThrow(() -> new NotFoundException("Custom Booking not found!"));
        return customBooking;
    }

    public CustomBooking deleteCusBooking(long id){
        CustomBooking oldCus = customBookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Breed not found!"));

        oldCus.setDeleted(true);
        return customBookingRepository.save(oldCus);
    }
}
