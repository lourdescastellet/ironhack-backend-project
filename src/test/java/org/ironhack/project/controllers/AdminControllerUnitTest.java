package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.AdminUpdateRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.services.AdminService;
import org.ironhack.project.dtos.AdminCreationRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class AdminControllerUnitTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

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

    @Test
    void create_validAdmin_adminCreated() throws Exception {
        AdminCreationRequest adminCreationRequest = new AdminCreationRequest();
        adminCreationRequest.setName("Admin A");
        adminCreationRequest.setEmail("admina@ironhack.com");
        adminCreationRequest.setPassword("password");

        Admin admin = new Admin();
        admin.setName(adminCreationRequest.getName());
        admin.setEmail(adminCreationRequest.getEmail());
        admin.setPassword(adminCreationRequest.getPassword());

        when(adminService.create(any(AdminCreationRequest.class))).thenReturn(admin);

        mockMvc.perform(post("/api/admin/new")
                        .content(objectMapper.writeValueAsString(adminCreationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin A"));
    }

    @Test
    void update_existingId_adminUpdated() throws Exception {
        AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest();
        adminUpdateRequest.setName("Updated Admin");
        adminUpdateRequest.setEmail("updated@ironhack.com");

        Admin updatedAdmin = new Admin();
        updatedAdmin.setUserId(1);
        updatedAdmin.setName(adminUpdateRequest.getName());
        updatedAdmin.setEmail(adminUpdateRequest.getEmail());
        updatedAdmin.setPassword(adminUpdateRequest.getPassword());

        when(adminService.update(anyInt(), any())).thenReturn(updatedAdmin);

        mockMvc.perform(put("/api/admin/{userId}/edit", 1)
                        .content(objectMapper.writeValueAsString(adminUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_adminDeleted() throws Exception {
        mockMvc.perform(delete("/api/admin/{userId}", 1))
                .andExpect(status().isNoContent());
    }
}