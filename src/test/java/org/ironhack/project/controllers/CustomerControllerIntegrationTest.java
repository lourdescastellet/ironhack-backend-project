package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private Customer customer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        customer = new Customer();
        customer.setName("Customer");
        customer.setEmail("customer@ironhack.com");
        customer.setPassword("password");
        customer.setCustomerAddress("Address 1");
        customer.setPaymentMethod("Visa");
        customer = customerRepository.save(customer);
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void findAll_returnsListOfCustomers() throws Exception {
        Customer customer1 = new Customer();
        customer1.setName("Customer 2");
        customer1.setEmail("customer2@example.com");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Customer 3");
        customer2.setEmail("customer3@example.com");
        customerRepository.save(customer2);

        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void findById_existingCustomerId_customerFound() throws Exception {

        mockMvc.perform(get("/api/customer/{userId}", customer.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Customer"))
                .andExpect(jsonPath("$.email").value("customer@ironhack.com"));
    }

    @Test
    void findById_nonExistingCustomerId_customerNotFound() throws Exception {
        mockMvc.perform(get("/api/customer/{userId}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingCustomerId_customerUpdated() throws Exception {

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
        updateRequest.setName("Updated Customer");
        updateRequest.setEmail("updated.customer@ironhack.com");

        String body = new ObjectMapper().writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/customer/{userId}/edit", customer.getUserId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Customer updatedCustomer = customerRepository.findById(customer.getUserId()).orElseThrow(() ->
                new IllegalStateException("Customer not found"));

        assertEquals("Updated Customer", updatedCustomer.getName());
        assertEquals("updated.customer@ironhack.com", updatedCustomer.getEmail());
    }
}
