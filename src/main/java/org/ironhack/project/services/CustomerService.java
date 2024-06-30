package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.CustomerRequest;
import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    public Customer create(@Valid CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(customerRequest.getPassword());
        customer.setPaymentMethod(customerRequest.getPaymentMethod());
        customer.setCustomerAddress(customerRequest.getCustomerAddress());

        return customerRepository.save(customer);
    }

    public Customer update(Integer customerId, @Valid CustomerUpdateRequest customerUpdateRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            if (customerUpdateRequest.getName() != null) {
                customer.setName(customerUpdateRequest.getName());
            }
            if (customerUpdateRequest.getEmail() != null) {
                customer.setEmail(customerUpdateRequest.getEmail());
            }
            if (customerUpdateRequest.getPassword() != null) {
                customer.setPassword(customerUpdateRequest.getPassword());
            }
            if (customerUpdateRequest.getPaymentMethod() != null) {
                customer.setPaymentMethod(customerUpdateRequest.getPaymentMethod());
            }
            if (customerUpdateRequest.getCustomerAddress() != null) {
                customer.setCustomerAddress(customerUpdateRequest.getCustomerAddress());
            }

            customerRepository.save(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with this User Id.");
        }

        return null;
    }

    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }
}
