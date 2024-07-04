package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.services.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VenueControllerUnitTest {

    @InjectMocks
    private VenueController venueController;

    @Mock
    private VenueService venueService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(venueController).build();
    }

    @Test
    void findAll_venuesReturned() throws Exception {
        Venue venue = new Venue();
        venue.setUserId(1);
        venue.setName("Venue A");
        venue.setEmail("venuea@example.com");
        venue.setPassword("password");
        venue.setVenueName("VENUE_A");
        venue.setVenueAddress("Address A");
        venue.setVenueCity("City A");
        venue.setVenueCapacity(100);

        when(venueService.findAll()).thenReturn(Arrays.asList(venue));

        mockMvc.perform(get("/api/venue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Venue A"));
    }

    @Test
    void findById_existingId_venueReturned() throws Exception {
        Venue venue = new Venue();
        venue.setUserId(1);
        venue.setName("Venue A");
        venue.setEmail("venuea@example.com");
        venue.setPassword("password");
        venue.setVenueName("VENUE_A");
        venue.setVenueAddress("Address A");
        venue.setVenueCity("City A");
        venue.setVenueCapacity(100);

        when(venueService.findById(1)).thenReturn(Optional.of(venue));

        mockMvc.perform(get("/api/venue/{venueId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Venue A"));
    }

    @Test
    void findById_nonExistingId_notFound() throws Exception {
        when(venueService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/venue/{venueId}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingId_venueUpdated() throws Exception {
        VenueUpdateRequest venueUpdateRequest = new VenueUpdateRequest();
        venueUpdateRequest.setName("Updated Venue");
        venueUpdateRequest.setEmail("updatedvenue@example.com");
        venueUpdateRequest.setPassword("newpassword");
        venueUpdateRequest.setVenueName("Updated Venue Name");
        venueUpdateRequest.setVenueAddress("Updated Address");
        venueUpdateRequest.setVenueCity("Updated City");
        venueUpdateRequest.setVenueCapacity(150);

        Venue venue = new Venue();
        venue.setUserId(1);
        venue.setName(venueUpdateRequest.getName());
        venue.setEmail(venueUpdateRequest.getEmail());
        venue.setPassword(venueUpdateRequest.getPassword());
        venue.setVenueName(venueUpdateRequest.getVenueName());
        venue.setVenueAddress(venueUpdateRequest.getVenueAddress());
        venue.setVenueCity(venueUpdateRequest.getVenueCity());
        venue.setVenueCapacity(venueUpdateRequest.getVenueCapacity());

        when(venueService.update(anyInt(), any(VenueUpdateRequest.class))).thenReturn(venue);

        mockMvc.perform(put("/api/venue/{venueId}/edit", 1)
                        .content(objectMapper.writeValueAsString(venueUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_venueDeleted() throws Exception {
        mockMvc.perform(delete("/api/venue/{venueId}", 1))
                .andExpect(status().isNoContent());
    }
}