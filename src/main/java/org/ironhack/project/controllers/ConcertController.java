package org.ironhack.project.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.dtos.ConcertCreationRequest;
import org.ironhack.project.dtos.ConcertResponseDTO;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping
    public List<ConcertResponseDTO> findAll() {
        List<Concert> concerts = concertService.findAllConcerts();

        return concerts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ConcertResponseDTO mapToDTO(Concert concert) {
        ConcertResponseDTO dto = new ConcertResponseDTO();
        dto.setConcertId(concert.getConcertId());
        dto.setConcertName(concert.getConcertName());
        dto.setArtist(mapArtistToDTO(concert.getArtist()));
        dto.setVenue(mapVenueToDTO(concert.getVenue()));
        return dto;
    }

    private ArtistDTO mapArtistToDTO(Artist artist) {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName(artist.getArtistName());
        dto.setGenre(artist.getGenre());
        return dto;
    }

    private VenueDTO mapVenueToDTO(Venue venue) {
        VenueDTO dto = new VenueDTO();
        dto.setVenueName(venue.getVenueName());
        dto.setVenueAddress(venue.getVenueAddress());
        dto.setVenueCity(venue.getVenueCity());
        dto.setVenueCapacity(venue.getVenueCapacity());
        return dto;
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<Concert> findById(@PathVariable Integer concertId) {
        Optional<Concert> concert = concertService.findConcertById(concertId);
        return concert.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<Concert> create(@Valid @RequestBody ConcertCreationRequest concertCreationRequest) {
        Concert createdConcert = concertService.createConcert(concertCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConcert);
    }

    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer concertId) {
        try {
            concertService.deleteConcert(concertId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
