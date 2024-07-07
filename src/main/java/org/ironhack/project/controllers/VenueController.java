package org.ironhack.project.controllers;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/venue")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    public List<VenueDTO> findAll() {
        return venueService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<VenueDTO> findById(@PathVariable Integer userId) {
        Optional<VenueDTO> venue =venueService.findById(userId);
        return venue.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> updateVenue(@PathVariable Integer userId,
                                         @Valid @RequestBody VenueUpdateRequest venueUpdateRequest,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        try {
            Venue updatedVenue = venueService.update(userId, venueUpdateRequest);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error updating venue: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer userId) {
        venueService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
