package org.ironhack.project.controllers;


import jakarta.validation.Valid;
import org.ironhack.project.dtos.CustomerRequest;
import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Customer> findById(@PathVariable Integer userId) {
        Optional<Customer> customer = customerService.findById(userId);
        return customer.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<CustomerRequest> create(@Valid @RequestBody CustomerRequest customerRequest) {
        customerService.create(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRequest);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable Integer userId, @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.update(userId, customerUpdateRequest);
        return ResponseEntity.ok().build();
    }
}