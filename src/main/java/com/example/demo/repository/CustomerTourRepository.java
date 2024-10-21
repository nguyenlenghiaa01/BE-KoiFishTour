package com.example.demo.repository;

import com.example.demo.entity.CustomerTour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTourRepository extends JpaRepository<CustomerTour,Long> {
    CustomerTour findCustomerTourById(long id);
}
