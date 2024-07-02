package org.ironhack.project.controllers;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueRequest;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<Venue> findAll() {
        return venueService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Venue> findById(@PathVariable Integer userId) {
        Optional<Venue> venue =venueService.findById(userId);
        return venue.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<VenueRequest> createVenue(@Valid @RequestBody VenueRequest venueRequest) {
        venueService.create(venueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(venueRequest);
    }

    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> updateVenue(@PathVariable Integer userId,
                                         @Valid @RequestBody VenueUpdateRequest venueUpdateRequest,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        Venue updatesVenue = venueService.update(userId, venueUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer userId) {
        venueService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
