package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminRequest;
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
                        @Valid AdminRequest adminRequest) {

        Optional<Admin> optionalAdmin = adminRepository.findById(userId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            if (adminRequest.getName() != null) {
                admin.setName(adminRequest.getName());
            }
            if (adminRequest.getEmail() != null) {
                admin.setEmail(adminRequest.getEmail());
            }
            if (adminRequest.getPassword() != null) {
                admin.setPassword(adminRequest.getPassword());
            }
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found with this User Id.");
        }
    }


    public Admin create(@Valid AdminRequest adminRequest) {
        Admin admin = new Admin();
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());

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
