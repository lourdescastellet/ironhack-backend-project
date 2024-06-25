package org.ironhack.project.controllers;


import jakarta.validation.Valid;
import org.ironhack.project.dtos.AdminDTO;
import org.ironhack.project.models.classes.Admin;
import org.ironhack.project.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public List<Admin> findAll() {
        return adminService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Admin> findById(@PathVariable Integer userId) {
        Optional<Admin> admin = adminService.findById(userId);
        return admin.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> update(@PathVariable Integer userId, @Valid @RequestBody AdminDTO adminDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        Admin updatedAdmin = adminService.update(userId, adminDTO);
        return ResponseEntity.ok(updatedAdmin);
    }

    @PostMapping("/new")
    public ResponseEntity<Admin> create(@RequestBody Admin admin) {
        Admin newAdmin =adminService.create(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdmin);
    }



}
