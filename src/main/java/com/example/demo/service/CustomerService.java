package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Consulting;
import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.ConsultingRequest;
import com.example.demo.model.CustomerRequest;
import com.example.demo.repository.ConsultingRepository;
import com.example.demo.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ModelMapper modelMapper;
    public Customer createNewCustomer(CustomerRequest customerRequest){
        // add Customer vào database bằng repository
        try {
            Customer customers = modelMapper.map(customerRequest, Customer.class);
            // xac dinh account nao tai ai student nao
            // thong qua duoc filter
            // filter luu lai duoc account yeu cau tao student nay r
            Account accountRequest=authenticationService.getCurrentAccount();
            customers.setAccount(accountRequest);
            Customer newConsulting = customerRepository.save(customers);
            return newConsulting; // trả về 1 thằng mới
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate customer code!");
        }
    }
    public List<Customer> getAllCustomer(){
        // lay tat ca Customer trong DB
        List<Customer> customers = customerRepository.findCustomersByIsDeletedFalse();
        return customers;
    }
    public Customer updateCustomer(Customer customer, long id){

        Customer oldCustomer = customerRepository.findCustomerById(id);
        if(oldCustomer ==null){
            throw new NotFoundException("Customer not found !");//dung viec xu ly ngay tu day
        }
        //=> co farm co ton tai;
        oldCustomer.setPhone(customer.getPhone());
        oldCustomer.setEmail(customer.getEmail());
        oldCustomer.setFullName(customer.getFullName());
        oldCustomer.setAddress(customer.getAddress());
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
