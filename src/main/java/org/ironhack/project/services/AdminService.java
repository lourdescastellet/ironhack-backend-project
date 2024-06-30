package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminRequest;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Admin update(Integer userId, AdminRequest adminRequest) {
        Admin existingAdmin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found with this user id."));

        existingAdmin.setName(adminRequest.getName());
        existingAdmin.setEmail(adminRequest.getEmail());
        existingAdmin.setPassword(adminRequest.getPassword());

        return adminRepository.save(existingAdmin);
    }

    public Admin create(@Valid AdminRequest adminRequest) {
        Admin admin = new Admin();
        admin.setName(adminRequest.getName());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());

        return adminRepository.save(admin);
    }

    public void deleteById(Integer userId) {
        adminRepository.deleteById(userId);
    }
}
