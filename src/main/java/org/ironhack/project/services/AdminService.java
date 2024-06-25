package org.ironhack.project.services;

import org.ironhack.project.dtos.AdminDTO;
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

    public Optional<Admin> findById(Integer id) {
        return adminRepository.findById(id);
    }

    public Admin update(Integer userId, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found with this user id."));

        existingAdmin.setUserName(adminDTO.getUserName());
        existingAdmin.setName(adminDTO.getName());
        existingAdmin.setEmail(adminDTO.getEmail());
        existingAdmin.setPassword(adminDTO.getPassword());

        return adminRepository.save(existingAdmin);
    }

    public Admin create(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteById(Integer id) {
        adminRepository.deleteById(id);
    }
}
