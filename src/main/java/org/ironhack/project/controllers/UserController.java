package org.ironhack.project.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ironhack.project.dtos.*;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.ironhack.project.services.AdminService;
import org.ironhack.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ArtistRepository artistRepository;
    private final VenueRepository venueRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final UserService userService;


    @PostMapping("/customer")
    public String registerCustomer(@Validated @RequestBody CustomerCreationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(hashedPassword);
        customer.setCustomerAddress(request.getCustomerAddress());
        customer.setPaymentMethod(request.getPaymentMethod());
        customerRepository.save(customer);
        return "Customer registered successfully";
    }

    @PostMapping("/admin")
    public String registerAdmin(@Validated @RequestBody AdminCreationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(hashedPassword);
        adminRepository.save(admin);
        return "Admin registered successfully";
    }

    @PostMapping("/artist")
    public String registerArtist(@Validated @RequestBody ArtistCreationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Artist artist = new Artist();
        artist.setName(request.getName());
        artist.setEmail(request.getEmail());
        artist.setPassword(hashedPassword);
        artist.setArtistName(request.getArtistName());
        artist.setGenre(request.getGenre());
        artistRepository.save(artist);
        return "Artist registered successfully";
    }

    @PostMapping("/venue")
    public String registerVenue(@Validated @RequestBody VenueCreationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Venue venue = new Venue();
        venue.setName(request.getName());
        venue.setEmail(request.getEmail());
        venue.setPassword(hashedPassword);
        venue.setVenueName(request.getVenueName());
        venue.setVenueAddress(request.getVenueAddress());
        venue.setVenueCity(request.getVenueCity());
        venue.setVenueCapacity(request.getVenueCapacity());
        venueRepository.save(venue);
        return "Venue registered successfully";
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId, Principal principal) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user: " + e.getMessage());
        }
    }
}