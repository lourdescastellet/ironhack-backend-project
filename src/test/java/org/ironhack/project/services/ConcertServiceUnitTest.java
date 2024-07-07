package org.ironhack.project.services;

import jakarta.persistence.EntityNotFoundException;
import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.dtos.ConcertCreationRequest;
import org.ironhack.project.dtos.ConcertResponseDTO;
import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.ArtistRepository;
import org.ironhack.project.repositories.ConcertRepository;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ConcertServiceUnitTest {

    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private VenueRepository venueRepository;

    @Autowired
    private TicketService ticketService;

    @InjectMocks
    private ConcertService concertService;

    private Concert concert1;
    private Concert concert2;

    @BeforeEach
    void setUp() {
        concert1 = new Concert();
        concert1.setConcertId(1);
        concert1.setConcertName("Concert 1");


        concert2 = new Concert();
        concert2.setConcertId(2);
        concert2.setConcertName("Concert 2");
    }

    @Test
    void findAll_multipleConcerts_foundAllConcerts() {
        when(concertRepository.findAll()).thenReturn(Arrays.asList(concert1, concert2));

        List<Concert> foundConcerts = concertService.findAllConcerts();

        assertThat(foundConcerts).hasSize(2);
        assertThat(foundConcerts).contains(concert1, concert2);
    }

    @Test
    void findConcertById_existingConcertId_concertFound() {
        when(concertRepository.findById(1)).thenReturn(Optional.of(concert1));

        Optional<Concert> foundConcert = concertService.findConcertById(1);

        assertThat(foundConcert).isPresent();
        assertThat(foundConcert.get()).isEqualTo(concert1);
    }

    @Test
    void findConcertById_nonExistingConcertId_concertNotFound() {
        when(concertRepository.findById(3)).thenReturn(Optional.empty());

        Optional<Concert> foundConcert = concertService.findConcertById(3);

        assertThat(foundConcert).isEmpty();
    }

    @Test
    void createConcert_validConcertRequest_concertCreated() {
        // Mock data for concert creation request
        ConcertCreationRequest concertRequest = new ConcertCreationRequest();
        concertRequest.setConcertName("New Concert");
        concertRequest.setArtistId(1);
        concertRequest.setVenueId(1);

        // Mock data for artist
        Artist artist = new Artist();
        artist.setUserId(1);
        artist.setArtistName("Artist A");

        // Mock data for venue
        Venue venue = new Venue();
        venue.setUserId(1);
        venue.setVenueName("Venue X");
        venue.setVenueCapacity(20);

        // Mock response from repositories
        when(artistRepository.findById(ArgumentMatchers.eq(1))).thenReturn(Optional.of(artist));
        when(venueRepository.findById(ArgumentMatchers.eq(1))).thenReturn(Optional.of(venue));
        when(concertRepository.save(ArgumentMatchers.any(Concert.class))).thenAnswer(invocation -> {
            Concert concert = invocation.getArgument(0);
            concert.setConcertId(1); // Simulate saving concert
            return concert;
        });

        // Mock the response from ticketService.generateTicketsForConcert if needed
//         BigDecimal originalPrice = BigDecimal.valueOf(75);
//        when(ticketService.generateTicketsForConcert(any(Concert.class), any(BigDecimal.class))).thenReturn(/* mocked tickets */);

        // Invoke the service method
        ConcertResponseDTO createdConcertDTO = concertService.createConcert(concertRequest);

        // Assert the created concert DTO is not null
        assertNotNull(createdConcertDTO);

        // Assert specific attributes of the created concert DTO
        assertEquals("New Concert", createdConcertDTO.getConcertName());
        assertEquals("Artist A", createdConcertDTO.getArtist().getArtistName());
        assertEquals("Venue X", createdConcertDTO.getVenue().getVenueName());
    }


    @Test
    void deleteConcert_existingConcertId_concertDeleted() {
        when(concertRepository.findById(concert1.getConcertId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> concertService.deleteConcert(concert1.getConcertId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Concert not found with id: " + concert1.getConcertId(), exception.getReason());
    }

    @Test
    void deleteConcert_nonExistingConcertId_concertNotFound() {
        when(concertRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> concertService.deleteConcert(3));
    }
}