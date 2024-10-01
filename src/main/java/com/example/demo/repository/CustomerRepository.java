package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findCustomerById(long id);
    List<Customer> findCustomersByIsDeletedFalse();
}
