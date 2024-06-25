package org.ironhack.project.controllers;


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

// TODO PUT request
//    @PutMapping("/{userId}/edit")
//    public ResponseEntity<Venue> update(@PathVariable Integer userId, @RequestBody Venue updatedVenue) {
//        Optional<Venue> venue = venueService.findById(userId);
//    }

    @PostMapping("/new")
    public ResponseEntity<Venue> create(@RequestBody Venue venue) {
        Venue newVenue =venueService.create(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVenue);
    }
}
