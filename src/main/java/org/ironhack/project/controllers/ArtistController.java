package org.ironhack.project.controllers;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<Artist> findAll() {
        return artistService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Artist> findById(@PathVariable Integer userId) {
        Optional<Artist> artist =artistService.findById(userId);
        return artist.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/edit")
    public ResponseEntity<?> updateArtist(@PathVariable Integer userId,
                                          @Valid @RequestBody ArtistUpdateRequest artistUpdateRequest,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
        }

        Artist updatedArtist = artistService.update(userId, artistUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer userId) {
        artistService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
