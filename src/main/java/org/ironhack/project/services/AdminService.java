package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminCreationRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findById(Integer userId) {
        return adminRepository.findById(userId);
    }

    public Admin update(Integer userId,
                        @Valid AdminCreationRequest adminCreationRequest) {

        Optional<Admin> optionalAdmin = adminRepository.findById(userId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            if (adminCreationRequest.getName() != null) {
                admin.setName(adminCreationRequest.getName());
            }
            if (adminCreationRequest.getEmail() != null) {
                admin.setEmail(adminCreationRequest.getEmail());
            }
            if (adminCreationRequest.getPassword() != null) {
                admin.setPassword(adminCreationRequest.getPassword());
            }
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with this User Id.");
        }
    }


    public Admin create(@Valid AdminCreationRequest adminCreationRequest) {
        Admin admin = new Admin();
        admin.setName(adminCreationRequest.getName());
        admin.setEmail(adminCreationRequest.getEmail());
        admin.setPassword(adminCreationRequest.getPassword());

        return adminRepository.save(admin);
    }

    public void deleteById(Integer userId) {
        Optional<Admin> optionalAdmin = adminRepository.findById(userId);
        optionalAdmin.ifPresentOrElse(
                admin -> adminRepository.delete(admin),
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with this User Id.");
                }
        );
    }
}
