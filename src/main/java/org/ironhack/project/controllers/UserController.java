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
@RequestMapping("/api/user")
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
    public ResponseEntity<?> registerCustomer(@Validated @RequestBody CustomerCreationRequest request) {
        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            Customer customer = new Customer();
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPassword(hashedPassword);
            customer.setCustomerAddress(request.getCustomerAddress());
            customer.setPaymentMethod(request.getPaymentMethod());
            Customer savedCustomer = customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register customer: " + e.getMessage());
        }
    }


    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@Validated @RequestBody AdminCreationRequest request) {
        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            Admin admin = new Admin();
            admin.setName(request.getName());
            admin.setEmail(request.getEmail());
            admin.setPassword(hashedPassword);
            Admin savedAdmin = adminRepository.save(admin);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register admin: " + e.getMessage());
        }
    }

    @PostMapping("/artist")
    public ResponseEntity<?> registerArtist(@Validated @RequestBody ArtistCreationRequest request) {
        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            Artist artist = new Artist();
            artist.setName(request.getName());
            artist.setEmail(request.getEmail());
            artist.setPassword(hashedPassword);
            artist.setArtistName(request.getArtistName());
            artist.setGenre(request.getGenre());
            Artist savedArtist = artistRepository.save(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register artist: " + e.getMessage());
        }
    }

    @PostMapping("/venue")
    public ResponseEntity<?> registerVenue(@Validated @RequestBody VenueCreationRequest request) {
        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            Venue venue = new Venue();
            venue.setName(request.getName());
            venue.setEmail(request.getEmail());
            venue.setPassword(hashedPassword);
            venue.setVenueName(request.getVenueName());
            venue.setVenueAddress(request.getVenueAddress());
            venue.setVenueCity(request.getVenueCity());
            venue.setVenueCapacity(request.getVenueCapacity());
            Venue savedVenue = venueRepository.save(venue);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVenue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register venue: " + e.getMessage());
        }
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