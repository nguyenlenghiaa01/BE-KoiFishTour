package com.example.demo.service;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomTourService {
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    CustomerTourRepository customerTourRepository;
    @Autowired
    FarmRepository farmRepository;
    public CustomTour createNewCus(CustomTourRequest customerTourRequest){
        CustomTour customerTour = modelMapper.map(customerTourRequest, CustomTour.class);
        try {
            CustomTour newCustomerTour = customerTourRepository.save(customerTour);
            return newCustomerTour;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Custom Tour id !");
        }

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

        oldCus.setEmail(customTourRequest.getEmail());
        oldCus.setDuration(customTourRequest.getDuration());
        oldCus.setBudget(customTourRequest.getBudget());
        oldCus.setPhone(customTourRequest.getPhone());
        oldCus.setFullName(customTourRequest.getFullName());
        oldCus.setStartDate(customTourRequest.getStartDate());
        oldCus.setAdult(customTourRequest.getAdult());
        oldCus.setChild(customTourRequest.getChild());
        oldCus.setInfant(customTourRequest.getInfant());
        oldCus.setFarms(customTourRequest.getFarm());
        oldCus.setStatus(customTourRequest.getStatus());
        return customerTourRepository.save(oldCus);
    }
    public CustomTour deleteCus(long id){
        CustomTour oldCus = customerTourRepository.findById(id).orElseThrow(() -> new NotFoundException("Breed not found!"));

        oldCus.setDeleted(true);
        return customerTourRepository.save(oldCus);
    }
}
