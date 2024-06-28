package org.ironhack.project.services;

import lombok.extern.slf4j.Slf4j;
import org.ironhack.project.models.classes.User;
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

    // Example method to find user based on email
    private User findUserByEmail(String email) {
        // Replace with your logic to find user based on email
        // Example: Check Admin, Customer, etc. tables or repositories
        // Return appropriate subclass instance
        return null; // Replace with actual implementation
    }


}
