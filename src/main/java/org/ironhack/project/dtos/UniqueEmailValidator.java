package org.ironhack.project.dtos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ironhack.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private VenueRepository venueRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true; //
        }
        return !adminRepository.existsByEmail(email) &&
                !customerRepository.existsByEmail(email) &&
                !artistRepository.existsByEmail(email) &&
                !venueRepository.existsByEmail(email);
    }
}
