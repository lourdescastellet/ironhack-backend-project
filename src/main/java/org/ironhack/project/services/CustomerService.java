package org.ironhack.project.services;

import org.ironhack.project.dtos.CustomerDTO;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Integer userId, CustomerDTO customerDTO) {
        Customer existingCustomer =customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found with this user id."));

       existingCustomer.setUserName(customerDTO.getUserName());
       existingCustomer.setName(customerDTO.getName());
       existingCustomer.setEmail(customerDTO.getEmail());
       existingCustomer.setPassword(customerDTO.getPassword());

        return  customerRepository.save(existingCustomer);
    }

    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }
}
