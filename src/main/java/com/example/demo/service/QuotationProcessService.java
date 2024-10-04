package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Breed;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationProcess;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BreedRequest;
import com.example.demo.model.Request.QuotationProcessRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.QuotationProcessRepository;
import com.example.demo.repository.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationProcessService {
    @Autowired
    QuotationProcessRepository quotationProcessRepository;

    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    AccountRepository accountRepository;

    private ModelMapper modelMapper = new ModelMapper();
    public QuotationProcess createNewQuotationProcess(QuotationProcessRequest quotationProcessRequest){
        //add breed vao database bang repsitory
        QuotationProcess quotationProcess = modelMapper.map(quotationProcessRequest, QuotationProcess.class);
        Quotation quotation = quotationRepository.findById(quotationProcessRequest.getQuotationId()).
                orElseThrow(() -> new NotFoundException("Quotation not exist!"));

        Account account = accountRepository.findById(quotationProcessRequest.getAccountId()).
                orElseThrow(() -> new NotFoundException("Account not exist!"));

        quotationProcess.setQuotation(quotation);
        quotationProcess.setAccount(account);
        try {
            QuotationProcess newQuotationProcess = quotationProcessRepository.save(quotationProcess);
            return newQuotationProcess;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate quotation process id !");
        }

    }
    public List<QuotationProcess> getAllQuotationProcess(){
        // lay tat ca student trong DB
        List<QuotationProcess> quotationProcesses = quotationProcessRepository.findQuotationProcessesByIsDeletedFalse();
        return quotationProcesses;
    }
    public QuotationProcess updateQuotationProcess(QuotationProcessRequest quotationProcess, long id){

        QuotationProcess oldQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);
        if(oldQuotationProcess ==null){
            throw new NotFoundException("Quotation Process not found !");//dung viec xu ly ngay tu day
        }
        //=> co Quotation Process co ton tai;
        oldQuotationProcess.setStatus(quotationProcess.getStatus());
        oldQuotationProcess.setNotes(quotationProcess.getNotes());
        return quotationProcessRepository.save(oldQuotationProcess);
    }
    public QuotationProcess deleteQuotationProcess(long id){
        QuotationProcess oldQuotationProcess = quotationProcessRepository.findQuotationProcessById(id);
        if(oldQuotationProcess ==null){
            throw new NotFoundException("Quotation Process not found !");//dung viec xu ly ngay tu day
        }
        oldQuotationProcess.setDeleted(true);
        return quotationProcessRepository.save(oldQuotationProcess);
    }
}
