package org.ironhack.project.services;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    public void deleteUserById_AdminUser_Success() {
        // Given
        Admin admin = new Admin(); // Create an admin user
        admin.setEmail("admin@example.com");
        adminRepository.save(admin);

        // When
        userService.deleteUserById(admin.getUserId());

        // Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            adminRepository.findById(admin.getUserId()).orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        });
    }

    @Test
    public void deleteUserById_ArtistUser_Success() {
        // Given
        Artist artist = new Artist(); // Create an artist user
        artist.setEmail("artist@example.com");
        artistRepository.save(artist);

        // When
        userService.deleteUserById(artist.getUserId());

        // Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            artistRepository.findById(artist.getUserId()).orElseThrow(() -> new EntityNotFoundException("Artist not found"));
        });
    }

    @Test
    public void deleteUserById_CustomerUser_Success() {
        // Given
        Customer customer = new Customer(); // Create a customer user
        customer.setEmail("customer@example.com");
        customerRepository.save(customer);

        // When
        userService.deleteUserById(customer.getUserId());

        // Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            customerRepository.findById(customer.getUserId()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        });
    }

    @Test
    public void deleteUserById_VenueUser_Success() {
        // Given
        Venue venue = new Venue(); // Create a venue user
        venue.setEmail("venue@example.com");
        venueRepository.save(venue);

        // When
        userService.deleteUserById(venue.getUserId());

        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            venueRepository.findById(venue.getUserId()).orElseThrow(() -> new EntityNotFoundException("Venue not found"));
        });
    }

}