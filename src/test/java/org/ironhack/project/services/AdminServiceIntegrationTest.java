package org.ironhack.project.services;

import org.ironhack.project.dtos.AdminCreationRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void create_validAdminRequest_adminCreated() {
        AdminCreationRequest adminCreationRequest = new AdminCreationRequest();
        adminCreationRequest.setName("Admin A");
        adminCreationRequest.setEmail("admina@ironhack.com");
        adminCreationRequest.setPassword("password");

        Admin savedAdmin = adminService.create(adminCreationRequest);

        assertNotNull(savedAdmin);
        assertNotNull(savedAdmin.getUserId());
        assertEquals("Admin A", savedAdmin.getName());
        assertEquals("admina@ironhack.com", savedAdmin.getEmail());
        assertEquals("password", savedAdmin.getPassword());
    }

    @Test
    void findById_existingAdminId_adminFound() {
        Admin adminToSave = new Admin();
        adminToSave.setName("Admin B");
        adminToSave.setEmail("adminb@ironhack.com");
        adminToSave.setPassword("password");
        Admin savedAdmin = adminRepository.save(adminToSave);

        Admin foundAdmin = adminService.findById(savedAdmin.getUserId()).orElse(null);

        assertNotNull(foundAdmin);
        assertEquals("Admin B", foundAdmin.getName());
        assertEquals("adminb@ironhack.com", foundAdmin.getEmail());
    }

    @Test
    void update_existingAdminId_adminUpdated() {
        Admin adminToSave = new Admin();
        adminToSave.setName("Original Admin");
        adminToSave.setEmail("original@ironhack.com");
        adminToSave.setPassword("password");
        Admin savedAdmin = adminRepository.save(adminToSave);

        AdminCreationRequest adminCreationRequest = new AdminCreationRequest();
        adminCreationRequest.setName("Updated Admin");
        adminCreationRequest.setEmail("updated@ironhack.com");

        Admin updatedAdmin = adminService.update(savedAdmin.getUserId(), adminCreationRequest);

        assertNotNull(updatedAdmin);
        assertEquals("Updated Admin", updatedAdmin.getName());
        assertEquals("updated@ironhack.com", updatedAdmin.getEmail());
        assertEquals("password", updatedAdmin.getPassword()); // Password should remain unchanged
    }

    @Test
    void delete_existingAdminId_adminDeleted() {
        Admin adminToSave = new Admin();
        adminToSave.setName("Admin to delete");
        adminToSave.setEmail("delete@ironhack.com");
        adminToSave.setPassword("password");
        Admin savedAdmin = adminRepository.save(adminToSave);

        adminService.deleteById(savedAdmin.getUserId());

        assertFalse(adminRepository.findById(savedAdmin.getUserId()).isPresent());
    }

    @Test
    void delete_nonExistingAdminId_adminNotFound() {
        assertThrows(ResponseStatusException.class, () -> adminService.deleteById(0)); // Assuming ID 0 does not exist
    }

    @Test
    void findAll_multipleAdmins_foundAllAdmins() {
        Admin admin1 = new Admin();
        admin1.setName("Admin A");
        admin1.setEmail("admina@ironhack.com");
        admin1.setPassword("password");
        adminRepository.save(admin1);

        Admin admin2 = new Admin();
        admin2.setName("Admin B");
        admin2.setEmail("adminb@ironhack.com");
        admin2.setPassword("password");
        adminRepository.save(admin2);

        List<Admin> foundAdmins = adminService.findAll();

        assertEquals(2, foundAdmins.size());
        assertEquals("Admin A", foundAdmins.get(0).getName());
        assertEquals("admina@ironhack.com", foundAdmins.get(0).getEmail());
        assertEquals("Admin B", foundAdmins.get(1).getName());
        assertEquals("adminb@ironhack.com", foundAdmins.get(1).getEmail());
    }
}
