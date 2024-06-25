package org.ironhack.project.controllers;

import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/concert")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping
    public List<Concert> findAll() {
        return concertService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Concert> findById(@PathVariable Integer userId) {
        Optional<Concert> concert = concertService.findById(userId);
        return concert.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<Concert> create(@RequestBody Concert concert) {
        Concert newConcert = concertService.create(concert);
        return ResponseEntity.status(HttpStatus.CREATED).body(newConcert);
    }
}
