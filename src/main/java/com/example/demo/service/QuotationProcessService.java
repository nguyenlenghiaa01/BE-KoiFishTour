package com.example.demo.service;

import com.example.demo.entity.Breed;
import com.example.demo.entity.QuotationProcess;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BreedRepository;
import com.example.demo.repository.QuotationProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationProcessService {
    @Autowired
    QuotationProcessRepository quotationProcessRepository;
    public QuotationProcess createNewQuotationProcess(QuotationProcess quotationProcess){
        //add breed vao database bang repsitory
        try {
            QuotationProcess newQuotationProcess = quotationProcessRepository.save(quotationProcess);
            return newQuotationProcess;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate breed id !");
        }

    }
    public List<QuotationProcess> getAllQuotationProcess(){
        // lay tat ca student trong DB
        List<QuotationProcess> quotationProcesses = quotationProcessRepository.findQuotationProcessesByIsDeletedFalse();
        return quotationProcesses;
    }
    public QuotationProcess updateQuotationProcess(QuotationProcess quotationProcess, long id){

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
