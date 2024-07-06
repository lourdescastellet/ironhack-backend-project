package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.dtos.ConcertCreationRequest;
import org.ironhack.project.dtos.ConcertResponseDTO;
import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.*;
import org.ironhack.project.services.ConcertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConcertControllerUnitTest {

    @InjectMocks
    private ConcertController concertController;

    @Mock
    private ConcertService concertService;

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private ConcertRepository concertRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist1;
    private Artist artist2;
    private Venue venue1;
    private Venue venue2;
    private Concert concert1;
    private Concert concert2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(concertController).build();

        artist1 = new Artist();
        artist1.setUserId(1);
        artist1.setArtistName("Artist A");
        artist1.setGenre(Genre.ROCK);

        artist2 = new Artist();
        artist2.setUserId(2);
        artist2.setArtistName("Artist B");
        artist2.setGenre(Genre.POP);

        venue1 = new Venue();
        venue1.setUserId(4);
        venue1.setVenueName("Venue X");
        venue1.setVenueCapacity(500);

        venue2 = new Venue();
        venue2.setUserId(5);
        venue2.setVenueName("Venue Y");
        venue2.setVenueCapacity(1500);

        concert1 = new Concert();
        concert1.setConcertId(1);
        concert1.setConcertName("Concert 1");
        concert1.setArtist(artist1);
        concert1.setVenue(venue1);

        concert2 = new Concert();
        concert2.setConcertId(2);
        concert2.setConcertName("Concert 2");
        concert2.setArtist(artist2);
        concert2.setVenue(venue2);
    }

    @Test
    void findAll_concertsFound() throws Exception {
        when(concertService.findAllConcerts()).thenReturn(List.of(concert1, concert2));

        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // Verify Concert 1
                .andExpect(jsonPath("$[0].concertId").value(1))
                .andExpect(jsonPath("$[0].concertName").value("Concert 1"))
                .andExpect(jsonPath("$[0].artist.artistName").value("Artist A"))
                .andExpect(jsonPath("$[0].artist.genre").value("ROCK"))
                .andExpect(jsonPath("$[0].venue.venueName").value("Venue X"))

                // Verify Concert 2
                .andExpect(jsonPath("$[1].concertId").value(2))
                .andExpect(jsonPath("$[1].concertName").value("Concert 2"))
                .andExpect(jsonPath("$[1].artist.artistName").value("Artist B"))
                .andExpect(jsonPath("$[1].artist.genre").value("POP"))
                .andExpect(jsonPath("$[1].venue.venueName").value("Venue Y"));
    }

    @Test
    void findById_existingId_concertReturned() throws Exception {
        when(concertService.findConcertById(1)).thenReturn(Optional.of(concert1));

        mockMvc.perform(get("/api/concerts/{concertId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertId").value(1))
                .andExpect(jsonPath("$.concertName").value("Concert 1"))
                .andExpect(jsonPath("$.artist.artistName").value("Artist A"))
                .andExpect(jsonPath("$.artist.genre").value("ROCK"))
                .andExpect(jsonPath("$.venue.venueName").value("Venue X"));
    }


    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(concertService.findConcertById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/concerts/{concertId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void createConcert_validConcertRequest_concertCreated() {
        ConcertCreationRequest concertRequest = new ConcertCreationRequest();
        concertRequest.setConcertName("New Concert");
        concertRequest.setArtistId(1);
        concertRequest.setVenueId(1);

        // Mock Artist and Venue entities
        Artist artist = new Artist();
        artist.setUserId(1);
        artist.setArtistName("Artist A");

        Venue venue = new Venue();
        venue.setUserId(1);
        venue.setVenueName("Venue X");
        venue.setVenueCapacity(20);

        // Mock ConcertResponseDTO to be returned by concertService.createConcert
        ConcertResponseDTO concertResponseDTO = new ConcertResponseDTO();
        concertResponseDTO.setConcertId(1);
        concertResponseDTO.setConcertName("New Concert");
        concertResponseDTO.setArtist(new ArtistDTO(artist.getArtistName()));
        concertResponseDTO.setVenue(new VenueDTO(venue.getVenueName(), venue.getVenueAddress(), venue.getVenueCity(), venue.getVenueCapacity()));

        // Mock repositories and concertService behavior
        when(artistRepository.findById(1)).thenReturn(Optional.of(artist));
        when(venueRepository.findById(1)).thenReturn(Optional.of(venue));
        when(concertRepository.save(any(Concert.class))).thenReturn(new Concert());
        when(concertService.createConcert(any())).thenReturn(concertResponseDTO); // Mock the service method

        // Call the service method
        ConcertResponseDTO createdConcertDTO = concertService.createConcert(concertRequest);

        // Assertions
        assertNotNull(createdConcertDTO);
        assertEquals(1, createdConcertDTO.getConcertId());
        assertEquals("New Concert", createdConcertDTO.getConcertName());
    }


    @Test
    void delete_existingConcert_concertDeleted() throws Exception {
        Concert concertToDelete = new Concert();
        concertToDelete.setConcertId(1);

        when(concertService.findConcertById(1)).thenReturn(Optional.of(concertToDelete));
        doNothing().when(concertService).deleteConcert(1);

        mockMvc.perform(delete("/api/concerts/{concertId}", 1))
                .andExpect(status().isNoContent());

        verify(concertService, times(1)).deleteConcert(1);
    }

    @Test
    void delete_nonExistingConcert_concertNotFound() throws Exception {
        when(concertService.findConcertById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/concerts/{concertId}", 1))
                .andExpect(status().isNoContent());
    }
}