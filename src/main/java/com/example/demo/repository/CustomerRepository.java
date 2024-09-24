package com.example.demo.repository;
import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer, String>{
    Customer findCustomerById( String CustomerId);
    List<Customer>findCustomersByIsDeletedFalse();
}
