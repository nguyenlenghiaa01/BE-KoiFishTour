package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationProcess;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.QuotationProcessRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.QuotationProcessRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

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
        // Create a new QuotationProcess entity
        QuotationProcess quotationProcess = new QuotationProcess();

        // Fetch Quotation and Account entities
        Quotation quotation = quotationRepository.findById(quotationProcessRequest.getQuotationId())
                .orElseThrow(() -> new NotFoundException("Quotation not found!"));
        Account account = accountRepository.findById(quotationProcessRequest.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found!"));

        // Set the values based on the request object
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

    public List<QuotationProcess> getAllQuotationProcess() {

        return quotationProcessRepository.findQuotationProcessesByIsDeletedFalse();
    }

    public QuotationProcess updateQuotationProcess(QuotationProcessRequest quotationProcessRequest, long id) {
        // Find the existing QuotationProcess by ID
        QuotationProcess existingQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);

        if (existingQuotationProcess == null) {
            throw new NotFoundException("Quotation Process not found!");
        }

        // Update the existing QuotationProcess
        existingQuotationProcess.setStatus(quotationProcessRequest.getStatus());
        existingQuotationProcess.setNotes(quotationProcessRequest.getNotes());

        return quotationProcessRepository.save(existingQuotationProcess);
    }

    public QuotationProcess deleteQuotationProcess(long id) {
        // Find the QuotationProcess by ID
        QuotationProcess existingQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);

        if (existingQuotationProcess == null) {
            throw new NotFoundException("Quotation Process not found!");
        }

        // Mark it as deleted
        existingQuotationProcess.setDeleted(true);

        return quotationProcessRepository.save(existingQuotationProcess);
    }
}


