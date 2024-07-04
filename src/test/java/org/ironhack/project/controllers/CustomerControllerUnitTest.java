package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.CustomerUpdateRequest;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerUnitTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void findAll_customersReturned() throws Exception {
        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setName("Customer A");
        customer.setEmail("customera@ironhack.com");

        when(customerService.findAll()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Customer A"));
    }

    @Test
    void findById_existingId_customerReturned() throws Exception {
        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setName("Customer A");
        customer.setEmail("customera@ironhack.com");

        when(customerService.findById(1)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customer/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Customer A"));
    }

    @Test
    void findById_nonExistingId_notFound() throws Exception {
        when(customerService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customer/{userId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingId_customerUpdated() throws Exception {
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setName("Customer A Updated");
        customerUpdateRequest.setEmail("updated@ironhack.com");
        customerUpdateRequest.setPassword("newpassword123");

        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setName(customerUpdateRequest.getName());
        customer.setEmail(customerUpdateRequest.getEmail());
        customer.setPassword(customerUpdateRequest.getPassword());

        when(customerService.update(anyInt(), any(CustomerUpdateRequest.class))).thenReturn(customer);

        mockMvc.perform(put("/api/customer/{userId}/edit", 1)
                        .content(objectMapper.writeValueAsString(customerUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_customerDeleted() throws Exception {
        mockMvc.perform(delete("/api/customer/{userId}", 1))
                .andExpect(status().isNoContent());
    }
}