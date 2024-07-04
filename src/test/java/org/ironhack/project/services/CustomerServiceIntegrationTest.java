package org.ironhack.project.services;

import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void findAll_multipleCustomers_foundAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer A");
        customer1.setEmail("customera@ironhack.com");
        customer1.setPassword("password");
        customer1.setPaymentMethod("Credit Card");
        customer1.setCustomerAddress("Address A");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Customer B");
        customer2.setEmail("customerb@ironhack.com");
        customer2.setPassword("password");
        customer2.setPaymentMethod("PayPal");
        customer2.setCustomerAddress("Address B");
        customerRepository.save(customer2);

        List<Customer> foundCustomers = customerService.findAll();

        assertEquals(2, foundCustomers.size());
        assertEquals("Customer A", foundCustomers.get(0).getName());
        assertEquals("customera@ironhack.com", foundCustomers.get(0).getEmail());
        assertEquals("Customer B", foundCustomers.get(1).getName());
        assertEquals("customerb@ironhack.com", foundCustomers.get(1).getEmail());
    }

    @Test
    void findById_existingCustomerId_customerFound() {
        Customer customerToSave = new Customer();
        customerToSave.setName("Found Customer");
        customerToSave.setEmail("found@ironhack.com");
        customerToSave.setPassword("password");
        customerToSave.setPaymentMethod("Credit Card");
        customerToSave.setCustomerAddress("Found Address");
        Customer savedCustomer = customerRepository.save(customerToSave);

        Optional<Customer> foundCustomer = customerService.findById(savedCustomer.getUserId());

        assertTrue(foundCustomer.isPresent());
        assertEquals("Found Customer", foundCustomer.get().getName());
    }

    @Test
    void findById_nonExistingCustomerId_customerNotFound() {
        Optional<Customer> foundCustomer = customerService.findById(0); // Assuming ID 0 does not exist

        assertFalse(foundCustomer.isPresent());
    }

    @Test
    void update_existingCustomerId_customerUpdated() {
        Customer customerToSave = new Customer();
        customerToSave.setName("Original Customer");
        customerToSave.setEmail("original@ironhack.com");
        customerToSave.setPassword("password");
        customerToSave.setPaymentMethod("Credit Card");
        customerToSave.setCustomerAddress("Original Address");
        Customer savedCustomer = customerRepository.save(customerToSave);

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setName("Updated Customer");
        customerUpdateRequest.setEmail("updated@ironhack.com");
        customerUpdateRequest.setPaymentMethod("PayPal");
        customerUpdateRequest.setCustomerAddress("Updated Address");

        Customer updatedCustomer = customerService.update(savedCustomer.getUserId(), customerUpdateRequest);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Customer", updatedCustomer.getName());
        assertEquals("updated@ironhack.com", updatedCustomer.getEmail());
        assertEquals("PayPal", updatedCustomer.getPaymentMethod());
        assertEquals("Updated Address", updatedCustomer.getCustomerAddress());
    }

    @Test
    void update_nonExistingCustomerId_customerNotFound() {
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setName("Updated Customer");
        customerUpdateRequest.setEmail("updated@ironhack.com");

        assertThrows(ResponseStatusException.class, () -> customerService.update(0, customerUpdateRequest)); // Assuming ID 0 does not exist
    }

    @Test
    void delete_existingCustomerId_customerDeleted() {
        Customer customerToSave = new Customer();
        customerToSave.setName("Customer to delete");
        customerToSave.setEmail("delete@ironhack.com");
        customerToSave.setPassword("password");
        customerToSave.setPaymentMethod("Credit Card");
        customerToSave.setCustomerAddress("Delete Address");
        Customer savedCustomer = customerRepository.save(customerToSave);

        customerService.deleteById(savedCustomer.getUserId());

        assertFalse(customerRepository.findById(savedCustomer.getUserId()).isPresent());
    }

    @Test
    void delete_nonExistingCustomerId_customerNotFound() {
        assertThrows(ResponseStatusException.class, () -> customerService.deleteById(0)); // Assuming ID 0 does not exist
    }
}
