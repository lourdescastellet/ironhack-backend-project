package org.ironhack.project.services;

import org.ironhack.project.dtos.ConcertCreationRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.ConcertRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.ironhack.project.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private TicketService ticketService;

    public List<Concert> findAllConcerts() {
        return concertRepository.findAll();
    }

    public Optional<Concert> findConcertById(Integer concertId) {
        return concertRepository.findById(concertId);
    }

    public Concert createConcert(ConcertCreationRequest concertRequest) {
        Concert concert = new Concert();
        concert.setConcertName(concertRequest.getConcertName());

        Artist artist = artistRepository.findById(concertRequest.getArtistId())
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        concert.setArtist(artist);

        Venue venue = venueRepository.findById(concertRequest.getVenueId())
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        concert.setVenue(venue);

        concert = concertRepository.save(concert);

        // Generate tickets for the concert
        BigDecimal originalPrice = BigDecimal.valueOf(75);
        ticketService.generateTicketsForConcert(concert, originalPrice); // Adjust original price as needed

        return concert;
    }

    public void deleteConcert(Integer concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Concert not found with id: " + concertId));

        concertRepository.delete(concert);
    }

}
