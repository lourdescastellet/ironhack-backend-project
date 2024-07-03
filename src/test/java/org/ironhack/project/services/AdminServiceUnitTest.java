package org.ironhack.project.services;

import org.ironhack.project.dtos.AdminCreationRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
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
class AdminServiceUnitTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_multipleAdmins_foundAllAdmins() {

        Admin admin1 = new Admin();
        admin1.setName("Admin A");
        admin1.setEmail("admina@ironhack.com");
        admin1.setPassword("password");

        Admin admin2 = new Admin();
        admin2.setName("Admin B");
        admin2.setEmail("adminb@ironhack.com");
        admin2.setPassword("password");

        when(adminRepository.findAll()).thenReturn(List.of(admin1, admin2));

        List<Admin> foundAdmins = adminService.findAll();

        assertEquals(2, foundAdmins.size());
        assertEquals("Admin A", foundAdmins.get(0).getName());
        assertEquals("admina@ironhack.com", foundAdmins.get(0).getEmail());
        assertEquals("Admin B", foundAdmins.get(1).getName());
        assertEquals("adminb@ironhack.com", foundAdmins.get(1).getEmail());
    }

    @Test
    void findById_existingAdminId_adminFound() {
        Integer userId = 1;
        Admin existingAdmin = new Admin();
        existingAdmin.setName("Found Admin");
        existingAdmin.setUserId(userId);

        when(adminRepository.findById(userId)).thenReturn(Optional.of(existingAdmin));

        Admin foundAdmin = adminService.findById(userId).orElse(null);

        assertNotNull(foundAdmin);
        assertEquals("Found Admin", foundAdmin.getName());
    }

    @Test
    void findById_nonExistingAdminId_adminNotFound() {
        Integer userId = 1;
        when(adminRepository.findById(userId)).thenReturn(Optional.empty());

        Admin foundAdmin = adminService.findById(userId).orElse(null);

        assertNull(foundAdmin);
    }

    @Test
    void create_validAdminRequest_adminCreated() {
        AdminCreationRequest adminCreationRequest = new AdminCreationRequest();
        adminCreationRequest.setName("Admin A");
        adminCreationRequest.setEmail("admina@ironhack.com");
        adminCreationRequest.setPassword("password");

        Admin adminToSave = new Admin();
        adminToSave.setName(adminCreationRequest.getName());
        adminToSave.setEmail(adminCreationRequest.getEmail());
        adminToSave.setPassword(adminCreationRequest.getPassword());

        when(adminRepository.save(any(Admin.class))).thenReturn(adminToSave);

        Admin savedAdmin = adminService.create(adminCreationRequest);

        assertNotNull(savedAdmin);
        assertEquals("Admin A", savedAdmin.getName());
        assertEquals("admina@ironhack.com", savedAdmin.getEmail());
    }

    @Test
    void update_existingAdminId_adminUpdated() {
        Integer userId = 1;
        AdminCreationRequest adminCreationRequest = new AdminCreationRequest();
        adminCreationRequest.setName("Updated Admin");
        adminCreationRequest.setEmail("updated@ironhack.com");

        Admin existingAdmin = new Admin();
        existingAdmin.setName("Original Admin");
        existingAdmin.setUserId(userId);
        existingAdmin.setPassword("password");
        existingAdmin.setEmail("original@ironhack.com");

        existingAdmin.setUserId(userId);

        when(adminRepository.findById(userId)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admin updatedAdmin = adminService.update(userId, adminCreationRequest);

        assertNotNull(updatedAdmin);
        assertEquals("Updated Admin", updatedAdmin.getName());
        assertEquals("updated@ironhack.com", updatedAdmin.getEmail());
        assertEquals("password", updatedAdmin.getPassword());
    }

    @Test
    void delete_existingAdminId_adminDeleted() {
        Admin adminToDelete = new Admin();
        Integer userId = 1;
        adminToDelete.setUserId(userId);

        when(adminRepository.findById(userId)).thenReturn(Optional.of(adminToDelete));
        doNothing().when(adminRepository).delete(any(Admin.class));

        adminService.deleteById(userId);

        verify(adminRepository, times(1)).findById(userId);
        verify(adminRepository, times(1)).delete(any(Admin.class));
    }

    @Test
    void delete_nonExistingAdminId_adminNotFound() {

        when(adminRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> adminService.deleteById(1));

    }
}