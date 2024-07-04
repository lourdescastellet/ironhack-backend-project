package org.ironhack.project.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ironhack.project.models.classes.*;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private VenueRepository venueRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("Attempting to load user by email: {}", email);

        User user = adminRepository.findByEmail(email);
        if (user == null) {
            user = customerRepository.findByEmail(email);
        }
        if (user == null) {
            user = artistRepository.findByEmail(email);
        }
        if (user == null) {
            user = venueRepository.findByEmail(email);
        }

        if (user == null) {
            log.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        log.info("User found: {}", user.getEmail());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void deleteUserById(Integer userId) {
        // Check and delete from AdminRepository
        Admin admin = adminRepository.findById(userId)
                .orElse(null);
        if (admin != null) {
            adminRepository.delete(admin);
            log.info("Admin with id {} deleted successfully", userId);
            return;
        }

        // Check and delete from ArtistRepository
        Artist artist = artistRepository.findById(userId)
                .orElse(null);
        if (artist != null) {
            artistRepository.delete(artist);
            log.info("Artist with id {} deleted successfully", userId);
            return;
        }

        // Check and delete from CustomerRepository
        Customer customer = customerRepository.findById(userId)
                .orElse(null);
        if (customer != null) {
            customerRepository.delete(customer);
            log.info("Customer with id {} deleted successfully", userId);
            return;
        }

        // Check and delete from VenueRepository
        Venue venue = venueRepository.findById(userId)
                .orElse(null);
        if (venue != null) {
            venueRepository.delete(venue);
            log.info("Venue with id {} deleted successfully", userId);
            return;
        }

        // If user not found in any repository
        throw new EntityNotFoundException("User not found with id: " + userId);
    }
}

