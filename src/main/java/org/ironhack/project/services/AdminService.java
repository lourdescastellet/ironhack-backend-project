package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminUpdateRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.CustomerRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                admin.setPassword(passwordEncoder.encode(adminUpdateRequest.getPassword()));
            }
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with this User Id.");
        }
    }
}
