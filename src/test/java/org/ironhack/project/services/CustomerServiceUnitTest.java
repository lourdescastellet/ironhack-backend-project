package org.ironhack.project.services;

import org.ironhack.project.dtos.CustomerRequest;
import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_multipleCustomers_foundAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setName("Customer A");
        customer1.setEmail("customera@ironhack.com");
        customer1.setPassword("password");

        Customer customer2 = new Customer();
        customer2.setName("Customer B");
        customer2.setEmail("customerb@ironhack.com");
        customer2.setPassword("password");

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> foundCustomers = customerService.findAll();

        assertEquals(2, foundCustomers.size());
        assertEquals("Customer A", foundCustomers.get(0).getName());
        assertEquals("customera@ironhack.com", foundCustomers.get(0).getEmail());
        assertEquals("Customer B", foundCustomers.get(1).getName());
        assertEquals("customerb@ironhack.com", foundCustomers.get(1).getEmail());
    }

    @Test
    void findById_existingCustomerId_customerFound() {
        Integer userId = 1;
        Customer existingCustomer = new Customer();
        existingCustomer.setUserId(userId);
        existingCustomer.setName("Found Customer");

        when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));

        Customer foundCustomer = customerService.findById(userId).orElse(null);

        assertNotNull(foundCustomer);
        assertEquals("Found Customer", foundCustomer.getName());
    }

    @Test
    void findById_nonExistingCustomerId_customerNotFound() {
        Integer userId = 1;

        when(customerRepository.findById(userId)).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findById(userId).orElse(null);

        assertNull(foundCustomer);
    }

    @Test
    void create_validCustomerRequest_customerCreated() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Customer A");
        customerRequest.setEmail("customera@ironhack.com");
        customerRequest.setPassword("password");

        Customer customerToSave = new Customer();
        customerToSave.setName(customerRequest.getName());
        customerToSave.setEmail(customerRequest.getEmail());
        customerToSave.setPassword(customerRequest.getPassword());

        when(customerRepository.save(any(Customer.class))).thenReturn(customerToSave);

        Customer savedCustomer = customerService.create(customerRequest);

        assertNotNull(savedCustomer);
        assertEquals("Customer A", savedCustomer.getName());
        assertEquals("customera@ironhack.com", savedCustomer.getEmail());
    }

    @Test
    void update_existingCustomerId_customerUpdated() {
        Integer userId = 1;
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setName("Updated Customer");
        customerUpdateRequest.setEmail("updated@ironhack.com");
        customerUpdateRequest.setPassword("password");

        Customer existingCustomer = new Customer();
        existingCustomer.setUserId(userId);
        existingCustomer.setName("Original Customer");
        existingCustomer.setEmail("original@ironhack.com");
        existingCustomer.setPassword("password");

        when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer updatedCustomer = customerService.update(userId, customerUpdateRequest);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Customer", updatedCustomer.getName());
        assertEquals("updated@ironhack.com", updatedCustomer.getEmail());
        assertEquals("password", updatedCustomer.getPassword());
    }

    @Test
    void delete_existingCustomerId_customerDeleted() {
        Customer customer = new Customer();
        customer.setUserId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));

        customerService.deleteById(1);

        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).delete(any(Customer.class));
    }

    @Test
    void delete_nonExistingCustomerId_customerNotFound() {
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> customerService.deleteById(1));
    }

}