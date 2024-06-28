package org.ironhack.project.controllers;


import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueRequest;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<VenueRequest> create(@Valid @RequestBody VenueRequest venueRequest) {
        venueService.create(venueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(venueRequest);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable Integer userId, @Valid @RequestBody VenueUpdateRequest venueUpdateRequest) {
        venueService.update(userId, venueUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer userId) {
        venueService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
