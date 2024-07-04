package org.ironhack.project.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminUpdateRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Customer;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.enums.Role;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.ironhack.project.models.enums.Role.*;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VenueRepository venueRepository;

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findById(Integer userId) {
        return adminRepository.findById(userId);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin update(Integer userId,
                        @Valid AdminUpdateRequest adminUpdateRequest) {

        Optional<Admin> optionalAdmin = adminRepository.findById(userId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            if (adminUpdateRequest.getName() != null) {
                admin.setName(adminUpdateRequest.getName());
            }
            if (adminUpdateRequest.getEmail() != null) {
                admin.setEmail(adminUpdateRequest.getEmail());
            }
            if (adminUpdateRequest.getPassword() != null) {
                admin.setPassword(adminUpdateRequest.getPassword());
            }
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with this User Id.");
        }
    }




}
