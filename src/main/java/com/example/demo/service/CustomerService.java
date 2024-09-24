package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    // xu ly nhung logic lien qua
    @Autowired
    CustomerRepository customerRepository;
    public Customer createNewCustomer(Customer customer){
        //add customer vao database bang repsitory
        try {
            Customer newCustomer = customerRepository.save(customer);
            return newCustomer;
        }catch (Exception  e){
            throw new DuplicateEntity("Duplicate Customer id !");
        }

    }
    public List<Customer> getAllCustomer(){
        // lay tat ca student trong DB
        List<Customer> customers = customerRepository.findCustomersByIsDeletedFalse();
        return customers;
    }
    public Customer updateCustomer(Customer customer, long customerId){
        // buoc 1: tim toi thang student co id nhu la FE cung cap
        Customer oldCustomer = customerRepository.findCustomerById(customerId);
        if(oldCustomer ==null){
            throw new NotFoundException("Customer not found !");//dung viec xu ly ngay tu day
        }
        //=> co student co ton tai;
        oldCustomer.setEmail(customer.getEmail());
        oldCustomer.setPhone(customer.getPhone());
        oldCustomer.setPassword(customer.getPassword());
        return customerRepository.save(oldCustomer);
    }
    public Customer deleteCustomer(long id){
        Customer oldCustomer = customerRepository.findCustomerById(id);
        if(oldCustomer ==null){
            throw new NotFoundException("Customer not found !");//dung viec xu ly ngay tu day
        }
        oldCustomer.setDeleted(true);
        return customerRepository.save(oldCustomer);
    }
}

