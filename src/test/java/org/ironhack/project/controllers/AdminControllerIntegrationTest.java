package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.AdminUpdateRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AdminControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AdminRepository adminRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Admin admin;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        admin = new Admin();
        admin.setName("Admin");
        admin.setEmail("admin@ironhack.com");
        admin.setPassword("password");
        admin = adminRepository.save(admin);
    }

    @AfterEach
    public void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void findAll_returnsListOfAdmins() throws Exception {
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findById_existingAdminId_adminFound() throws Exception {
        mockMvc.perform(get("/api/admin/{userId}", admin.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    void findById_nonExistingAdminId_adminNotFound() throws Exception {
        mockMvc.perform(get("/api/admin/{userId}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingAdminId_adminUpdated() throws Exception {
        AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest();
        adminUpdateRequest.setName("Updated Admin");

        String body = objectMapper.writeValueAsString(adminUpdateRequest);

        mockMvc.perform(put("/api/admin/{userId}/edit", admin.getUserId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Admin updated = adminRepository.findById(admin.getUserId()).orElseThrow(() ->
        new IllegalStateException("Admin not found"));

        assertEquals("Updated Admin", updated.getName());
        assertEquals("admin@ironhack.com", updated.getEmail());
        assertEquals("password", updated.getPassword());
    }

//    TODO update deleting tests
//    @Test
//    void delete_existingAdminId_adminDeleted() throws Exception {
//        mockMvc.perform(delete("/api/admin/{userId}", admin.getUserId()))
//                .andExpect(status().isNoContent());
//
//        Optional<Admin> deletedAdmin = adminRepository.findById(admin.getUserId());
//        assertFalse(deletedAdmin.isPresent());
//    }
//
//    @Test
//    void delete_nonExistingAdminId_notFound() throws Exception {
//        mockMvc.perform(delete("/api/admin/{userId}", 999))
//                .andExpect(status().isNotFound());
//    }
}
