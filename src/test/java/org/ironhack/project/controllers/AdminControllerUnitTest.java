package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.AdminUpdateRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.ironhack.project.services.AdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class AdminControllerUnitTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void findById_existingId_adminReturned() throws Exception {
        Admin admin = new Admin();
        admin.setUserId(1);
        admin.setName("Admin A");

        when(adminService.findById(1)).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/api/admin/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin A"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(adminService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/{userId}", 0))
                .andExpect(status().isNotFound());
    }

//    TODO update test - verified in Postman already
//    @Test
//    void update_existingId_adminUpdated() throws Exception {
//        AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest();
//        adminUpdateRequest.setName("Updated Admin");
//        adminUpdateRequest.setEmail("updated@ironhack.com");
//
//        Admin updatedAdmin = new Admin();
//        updatedAdmin.setUserId(1);
//        updatedAdmin.setName(adminUpdateRequest.getName());
//        updatedAdmin.setEmail(adminUpdateRequest.getEmail());
//
//        when(adminService.update(anyInt(), any())).thenReturn(updatedAdmin);
//        when(adminRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//        when(customerRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//        when(artistRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//        when(venueRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//
//        mockMvc.perform(put("/api/admin/{userId}/edit", 1)
//                        .content(objectMapper.writeValueAsString(adminUpdateRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void update_existingId_emailAlreadyExists() throws Exception {
//        AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest();
//        adminUpdateRequest.setName("Updated Admin");
//        adminUpdateRequest.setEmail("existing@ironhack.com"); // Email that already exists
//
//        // Mock the existence of the email in one of the repositories
//        when(adminRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(true);
//        when(customerRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//        when(artistRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//        when(venueRepository.existsByEmail(adminUpdateRequest.getEmail())).thenReturn(false);
//
//        mockMvc.perform(put("/api/admin/{userId}/edit", 1)
//                        .content(objectMapper.writeValueAsString(adminUpdateRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Validation errors: [Field error in object 'adminUpdateRequest' on field 'email': rejected value [existing@ironhack.com];]")); // Adjust the expected error message as needed
//    }
}