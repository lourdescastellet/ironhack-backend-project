package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String email);

    boolean existsByEmail(String email);
}
