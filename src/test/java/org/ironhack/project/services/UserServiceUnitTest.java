package org.ironhack.project.services;

import jakarta.persistence.EntityNotFoundException;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.enums.Role;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceUnitTest {


    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_AdminUserFound() {
        String email = "admin@example.com";
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setRoles(Collections.singleton(Role.ADMIN));

        when(adminRepository.findByEmail(email)).thenReturn(admin);

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN")));
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String email = "nonexistent@example.com";

        when(adminRepository.findByEmail(email)).thenReturn(null);
        when(customerRepository.findByEmail(email)).thenReturn(null);
        when(artistRepository.findByEmail(email)).thenReturn(null);
        when(venueRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void deleteUserById_AdminUserDeletedSuccessfully() {
        Integer userId = 1;
        Admin admin = new Admin();
        admin.setUserId(userId);

        when(adminRepository.findById(userId)).thenReturn(Optional.of(admin));

        userService.deleteUserById(userId);

        verify(adminRepository, times(1)).delete(admin);
    }

    @Test
    void deleteUserById_UserNotFound() {
        Integer userId = 1;

        when(adminRepository.findById(userId)).thenReturn(Optional.empty());
        when(artistRepository.findById(userId)).thenReturn(Optional.empty());
        when(customerRepository.findById(userId)).thenReturn(Optional.empty());
        when(venueRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(userId));
    }
}