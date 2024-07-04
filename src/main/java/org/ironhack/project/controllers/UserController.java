package org.ironhack.project.controllers;

import lombok.RequiredArgsConstructor;
import org.ironhack.project.dtos.RegistrationRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ArtistRepository artistRepository;
    private final VenueRepository venueRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/customer")
    public String registerCustomer(@RequestBody RegistrationRequest request) {
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
    public String registerAdmin(@RequestBody RegistrationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(hashedPassword);
        adminRepository.save(admin);
        return "Admin registered successfully";
    }

    @PostMapping("/artist")
    public String registerArtist(@RequestBody RegistrationRequest request) {
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
    public String registerVenue(@RequestBody RegistrationRequest request) {
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
}