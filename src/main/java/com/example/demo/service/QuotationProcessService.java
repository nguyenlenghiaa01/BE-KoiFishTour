package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationProcess;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.QuotationProcessRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.QuotationProcessRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuotationProcessService {
    @Autowired
    private QuotationProcessRepository quotationProcessRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public QuotationProcess createNewQuotationProcess(QuotationProcessRequest quotationProcessRequest){
        QuotationProcess quotationProcess = new QuotationProcess();
        Quotation quotation = quotationRepository.findById(quotationProcessRequest.getQuotationId())
                .orElseThrow(() -> new NotFoundException("Quotation not found!"));
        Account account = accountRepository.findById(quotationProcessRequest.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found!"));
        quotationProcess.setStatus(quotationProcessRequest.getStatus());
        quotationProcess.setNotes(quotationProcessRequest.getNotes());
        quotationProcess.setQuotation(quotation);
        quotationProcess.setAccount(account);

        try {
            return quotationProcessRepository.save(quotationProcess);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntity("Duplicate quotation process ID!");
        }
    }

    public DataResponse<QuotationProcess> getAllQuotation(@RequestParam int page, @RequestParam int size, @RequestParam long id) {
        Quotation quotation = quotationRepository.findById(id).orElseThrow(() -> new NotFoundException("Quotation not found!"));

        Page<QuotationProcess> quotationPage = quotationProcessRepository.findAll(PageRequest.of(page, size));
        List<QuotationProcess> quotations = new ArrayList<>();

        for (QuotationProcess quotationProcess : quotationPage.getContent()) {
            QuotationProcess updatedQuotationProcess = new QuotationProcess();
            updatedQuotationProcess.setNotes(quotationProcess.getNotes());
            updatedQuotationProcess.setStatus(quotation.getStatus());
            updatedQuotationProcess.setAccount(quotationProcess.getAccount());
            updatedQuotationProcess.setQuotationProcessId(quotationProcess.getQuotationProcessId());

            quotations.add(updatedQuotationProcess);
        }
        DataResponse<QuotationProcess> dataResponse = new DataResponse<>();
        dataResponse.setListData(quotations);
        dataResponse.setTotalElements(quotationPage.getTotalElements());
        dataResponse.setPageNumber(quotationPage.getNumber());
        dataResponse.setTotalPages(quotationPage.getTotalPages());

        return dataResponse;
    }



    public QuotationProcess updateQuotationProcess(QuotationProcessRequest quotationProcessRequest, long id) {
        QuotationProcess existingQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);
        if (existingQuotationProcess == null) {
            throw new NotFoundException("Quotation Process not found!");
        }
        existingQuotationProcess.setStatus(quotationProcessRequest.getStatus());
        existingQuotationProcess.setNotes(quotationProcessRequest.getNotes());
        return quotationProcessRepository.save(existingQuotationProcess);
    }

    public QuotationProcess deleteQuotationProcess(long id) {
        QuotationProcess existingQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);
        if (existingQuotationProcess == null) {
            throw new NotFoundException("Quotation Process not found!");
        }
        existingQuotationProcess.setDeleted(true);

        return quotationProcessRepository.save(existingQuotationProcess);
    }
}


