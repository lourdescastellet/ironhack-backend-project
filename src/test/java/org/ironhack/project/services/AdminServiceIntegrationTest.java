package org.ironhack.project.services;

import org.ironhack.project.dtos.AdminUpdateRequest;
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

        AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest();
        adminUpdateRequest.setName("Updated Admin");
        adminUpdateRequest.setEmail("updated@ironhack.com");

        Admin updatedAdmin = adminService.update(savedAdmin.getUserId(), adminUpdateRequest);

        assertNotNull(updatedAdmin);
        assertEquals("Updated Admin", updatedAdmin.getName());
        assertEquals("updated@ironhack.com", updatedAdmin.getEmail());
        assertEquals("password", updatedAdmin.getPassword());
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
