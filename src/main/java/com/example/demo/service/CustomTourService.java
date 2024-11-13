package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.CustomTour;
import com.example.demo.entity.Farm;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.CustomTourRequest;
import com.example.demo.model.Response.CustomTourResponse;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.repository.CustomerTourRepository;
import com.example.demo.repository.FarmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class CustomTourService {
    @Autowired
    CustomerTourRepository customerTourRepository;
    @Autowired
    FarmRepository farmRepository;

    @Autowired
    AuthenticationService authenticationService;
    public CustomTour createNewCus(CustomTourRequest customerTourRequest){
        Set<Farm> farms = new HashSet<>();
        Account account = authenticationService.getCurrentAccount();
        for (String farmId : customerTourRequest.getFarm()) {
            Farm getFarm = farmRepository.findByFarmId(farmId);

            if(getFarm != null) {
                farms.add(getFarm);
            }
        }
        CustomTour customerTour = new CustomTour();
        customerTour.setStartDate(customerTourRequest.getStartDate());
        customerTour.setDuration(customerTourRequest.getDuration());
        customerTour.setEmail(customerTourRequest.getEmail());
        customerTour.setPhone(customerTourRequest.getPhone());
        customerTour.setFullName(customerTourRequest.getFullName());
        customerTour.setAddress(customerTourRequest.getAddress());
        customerTour.setBudget(customerTourRequest.getBudget());
        customerTour.setCreateAt(new Date());
        customerTour.setStatus("PENDING");
        customerTour.setAdult(customerTourRequest.getAdult());
        customerTour.setChild(customerTourRequest.getChild());
        customerTour.setInfant(customerTourRequest.getInfant());
        customerTour.setCustomer(account);
        customerTour.setFarms(farms);
        return customerTourRepository.save(customerTour);
    }
    public DataResponse<CustomTourResponse> getAllCus(@RequestParam int page, @RequestParam int size){
        Page<CustomTour> cusPage = customerTourRepository.findAll(PageRequest.of(page, size));
        List<CustomTour> customTours = cusPage.getContent();
        List<CustomTourResponse> customTourResponses = new ArrayList<>();

        for(CustomTour customTour : customTours) {
            CustomTourResponse customTourResponse = new CustomTourResponse();
            customTourResponse.setId(customTour.getId());
            customTourResponse.setBudget(customTour.getBudget());
            customTourResponse.setAddress(customTour.getAddress());
            customTourResponse.setDuration(customTour.getDuration());
            customTourResponse.setStartDate(customTour.getStartDate());
            customTourResponse.setEmail(customTour.getEmail());
            customTourResponse.setPhone(customTour.getPhone());
            customTourResponse.setAdult(customTour.getAdult());
            customTourResponse.setChild(customTour.getChild());
            customTourResponse.setInfant(customTour.getInfant());
            customTourResponse.setStatus(customTour.getStatus());
            customTourResponse.setFarms(customTour.getFarms());
            customTourResponse.setCustomerName(customTour.getFullName());

            customTourResponses.add(customTourResponse);
        }

        DataResponse<CustomTourResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(customTourResponses);
        dataResponse.setTotalElements(cusPage.getTotalElements());
        dataResponse.setPageNumber(cusPage.getNumber());
        dataResponse.setTotalPages(cusPage.getTotalPages());

        return dataResponse;
    }

    public CustomTour updateCus(CustomTourRequest customTourRequest, long id){

        CustomTour oldCus = customerTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found!"));
        Set<Farm> farms = new HashSet<>();
        for(String farmCode : customTourRequest.getFarm()){
            Farm getFarm = farmRepository.findByFarmId(farmCode);
            if(getFarm != null) {
                farms.add(getFarm);
            }
        }
        oldCus.setEmail(customTourRequest.getEmail());
        oldCus.setDuration(customTourRequest.getDuration());
        oldCus.setBudget(customTourRequest.getBudget());
        oldCus.setPhone(customTourRequest.getPhone());
        oldCus.setFullName(customTourRequest.getFullName());
        oldCus.setStartDate(customTourRequest.getStartDate());
        oldCus.setAdult(customTourRequest.getAdult());
        oldCus.setChild(customTourRequest.getChild());
        oldCus.setInfant(customTourRequest.getInfant());
        oldCus.setFarms(farms);
        oldCus.setStatus("UPDATED");
        return customerTourRepository.save(oldCus);
    }
    public CustomTour deleteCus(long id){
        CustomTour oldCus = customerTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Breed not found!"));

        oldCus.setDeleted(true);
        return customerTourRepository.save(oldCus);
    }
}
