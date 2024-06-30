package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.services.AdminService;
import org.ironhack.project.dtos.AdminRequest;

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
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setName("Admin A");
        adminRequest.setEmail("admina@ironhack.com");
        adminRequest.setPassword("password");

        Admin admin = new Admin();
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());

        when(adminService.create(any(AdminRequest.class))).thenReturn(admin);

        mockMvc.perform(post("/api/admin/new")
                        .content(objectMapper.writeValueAsString(adminRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin A"));
    }

    @Test
    void update_existingId_adminUpdated() throws Exception {
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setName("Updated Admin");
        adminRequest.setEmail("updated@ironhack.com");
        adminRequest.setPassword("newpassword");

        Admin admin = new Admin();
        admin.setUserId(1);
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());

        when(adminService.update(anyInt(), any(AdminRequest.class))).thenReturn(admin);

        mockMvc.perform(put("/api/admin/{userId}/edit", 1)
                        .content(objectMapper.writeValueAsString(adminRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Admin"));
    }

    @Test
    void delete_existingId_adminDeleted() throws Exception {
        mockMvc.perform(delete("/api/admin/{userId}", 1))
                .andExpect(status().isNoContent());
    }
}