package org.ironhack.project.controllers;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.services.AdminService;
import org.ironhack.project.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private AdminService adminService;

    @GetMapping
    public List<Artist> findAll() {
        return artistService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Artist> findById(@PathVariable Integer userId) {
        Optional<Artist> artist =artistService.findById(userId);
        return artist.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<ArtistRequest> create(@Valid @RequestBody ArtistRequest artistRequest) {
        artistService.create(artistRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(artistRequest);
    }

}
