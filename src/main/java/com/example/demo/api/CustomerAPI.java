package com.example.demo.api;

import com.example.demo.entity.Consulting;
import com.example.demo.entity.Customer;
import com.example.demo.model.ConsultingRequest;
import com.example.demo.model.CustomerRequest;
import com.example.demo.service.ConsultingService;
import com.example.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CustomerAPI {
    @Autowired
    CustomerService customerService;


    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CustomerRequest customerRequest) {
        // nhờ service tạo mới 1 account
        Customer newCustomer = customerService.createNewCustomer(customerRequest);
        // return về cho front-end
        return ResponseEntity.ok(newCustomer);
    }

    // Get danh sách customer
    @GetMapping
    public ResponseEntity get() {
        List<Customer> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }

    // /api/customer/{id} => id của thằng customer mình muốn update
    @PutMapping("{id}")
    public ResponseEntity updateCustomer(@Valid @RequestBody Customer customer, @PathVariable long id) {
        Customer newCustomer = customerService.updateCustomer(customer,id);
        return ResponseEntity.ok(newCustomer);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCustomer(@PathVariable long id) {
        Customer newCustomer = customerService.deleteCustomer(id);
        return ResponseEntity.ok(newCustomer);
    }
}
