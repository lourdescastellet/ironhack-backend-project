package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer customer1 = new Customer();
        customer1.setName("Customer A");
        customer1.setEmail("customera@ironhack.com");
        customer1.setPassword("password");
        customer1.setPaymentMethod("Credit Card");
        customer1.setCustomerAddress("Street A");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Customer B");
        customer2.setEmail("customerb@ironhack.com");
        customer2.setPassword("password");
        customer2.setPaymentMethod("PayPal");
        customer2.setCustomerAddress("Street B");
        customerRepository.save(customer2);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void saveCustomer_newCustomer_customerSaved() {
        Customer customer = new Customer();
        customer.setName("Customer B");
        customer.setEmail("customerb@ironhack.com");
        customer.setPassword("password");
        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer);
        assertEquals("Customer B", savedCustomer.getName());
        assertEquals("customerb@ironhack.com", savedCustomer.getEmail());
        assertEquals("password", savedCustomer.getPassword());
    }

    @Test
    void findByEmail_existingEmail_customerReturned() {
        Customer found = customerRepository.findByEmail("customera@ironhack.com");
        assertNotNull(found);
        assertEquals("Customer A", found.getName());
        assertEquals("customera@ironhack.com", found.getEmail());
    }

    @Test
    void findByEmail_nonExistingEmail_nullReturned() {
        Customer found = customerRepository.findByEmail("nonexisting@ironhack.com");
        assertNull(found);
    }

    @Test
    void findByPaymentMethod_existingPaymentMethod_customersReturned() {
        List<Customer> customers = customerRepository.findByPaymentMethod("Credit Card");
        assertNotNull(customers);
        assertEquals(1, customers.size());
        Customer customer = customers.get(0);
        assertEquals("Customer A", customer.getName());
        assertEquals("customera@ironhack.com", customer.getEmail());
        assertEquals("password", customer.getPassword());
        assertEquals("Credit Card", customer.getPaymentMethod());
        assertEquals("Street A", customer.getCustomerAddress());
    }

    @Test
    void findByPaymentMethod_nonExistingPaymentMethod_emptyListReturned() {
        List<Customer> customers = customerRepository.findByPaymentMethod("Debit Card");
        assertNotNull(customers);
        assertEquals(0, customers.size());
    }

    @Test
    void findByCustomerAddress_existingAddress_customerReturned() {
        List<Customer> customers = customerRepository.findByCustomerAddress("Street B");
        assertNotNull(customers);
        assertEquals(1, customers.size());
        Customer customer = customers.get(0);
        assertEquals("Customer B", customer.getName());
        assertEquals("customerb@ironhack.com", customer.getEmail());
        assertEquals("password", customer.getPassword());
        assertEquals("PayPal", customer.getPaymentMethod());
        assertEquals("Street B", customer.getCustomerAddress());
    }

    @Test
    void findByCustomerAddress_nonExistingAddress_emptyListReturned() {
        List<Customer> customers = customerRepository.findByCustomerAddress("Street C");
        assertNotNull(customers);
        assertEquals(0, customers.size());
    }

}