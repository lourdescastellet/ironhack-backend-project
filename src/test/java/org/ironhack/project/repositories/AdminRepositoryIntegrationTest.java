package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Admin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminRepositoryIntegrationTest {

    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void saveAdmin_newAdmin_adminSaved() {
        Admin admin = new Admin();
        admin.setName("Admin B");
        admin.setEmail("adminb@ironhack.com");
        admin.setPassword("password");
        Admin savedAdmin = adminRepository.save(admin);
        assertNotNull(savedAdmin);
        assertEquals("Admin B", savedAdmin.getName());
        assertEquals("adminb@ironhack.com", savedAdmin.getEmail());
        assertEquals("password", savedAdmin.getPassword());
    }

    @Test
    void findByEmail_existingEmail_adminReturned() {
        Admin found = adminRepository.findByEmail("admina@ironhack.com");
        assertNotNull(found);
        assertEquals("Admin A", found.getName());
        assertEquals("admina@ironhack.com", found.getEmail());
    }

    @Test
    void findByEmail_nonExistingEmail_nullReturned() {
        Admin found = adminRepository.findByEmail("nonexisting@ironhack.com");
        assertNull(found);
    }

    @Test
    void findAll_existingAdmins_adminsReturned() {
        List<Admin> admins = adminRepository.findAll();
        assertNotNull(admins);
        assertEquals(2, admins.size());
        Admin adminA = admins.get(0);
        assertEquals("Admin A", adminA.getName());
        assertEquals("admina@ironhack.com", adminA.getEmail());
        assertEquals("password", adminA.getPassword());
        Admin adminB = admins.get(1);
        assertEquals("Admin B", adminB.getName());
        assertEquals("adminb@ironhack.com", adminB.getEmail());
        assertEquals("password", adminB.getPassword());
    }

    @Test
    void findAll_noAdmins_emptyListReturned() {
        adminRepository.deleteAll();
        List<Admin> admins = adminRepository.findAll();
        assertNotNull(admins);
        assertTrue(admins.isEmpty());
    }
}