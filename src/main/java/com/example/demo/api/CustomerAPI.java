package com.example.demo.api;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("*")
public class CustomerAPI {
    @Autowired
    CustomerService customerService;
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Customer customer) {
        Customer newCustomer = customerService.createNewCustomer(customer);
        //return ve font end
        return ResponseEntity.ok(newCustomer);
    }

    // Get danh sách sinh viên
    @GetMapping
    public ResponseEntity get(){
        List<Customer> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }
    // /api/student/{id} => id cua thang customer minh muon update
    @PutMapping("{id}")
    public ResponseEntity updateCustomer(@Valid @RequestBody Customer customer, @PathVariable long id){//valid kich hoat co che vadilation
        Customer newCustomer = customerService.updateCustomer(customer,id);
        return ResponseEntity.ok(newCustomer);
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteCustomer(@PathVariable long id){
        Customer newCustomer = customerService.deleteCustomer(id);
        return ResponseEntity.ok(newCustomer);
    }
}
