package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.VenueCreationRequest;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class VenueControllerIntegrationTest {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private Venue venue;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        venue = new Venue();
        venue.setName("Venue 1");
        venue.setEmail("venue@ironhack.com");
        venue.setPassword("password");
        venue.setVenueName("Venue1");
        venue.setVenueCity("Location 1");
        venue.setVenueAddress("Address 1");
        venue.setVenueCapacity(1000);
        venue = venueRepository.save(venue);
    }

    @AfterEach
    public void tearDown() {
        venueRepository.deleteAll();
    }

    @Test
    void createVenue_createsVenue() throws Exception {
        VenueCreationRequest venueCreationRequest = new VenueCreationRequest();
        venueCreationRequest.setName("New Venue");
        venueCreationRequest.setEmail("newvenue@ironhack.com");
        venueCreationRequest.setPassword("password");
        venueCreationRequest.setVenueName("New Venue 1");
        venueCreationRequest.setVenueCity("New Location");
        venueCreationRequest.setVenueAddress("New Address 1");
        venueCreationRequest.setVenueCapacity(2000);

        String body = new ObjectMapper().writeValueAsString(venueCreationRequest);

        mockMvc.perform(post("/api/venue/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Venue"))
                .andExpect(jsonPath("$.email").value("newvenue@ironhack.com"))
                .andExpect(jsonPath("$.venueName").value("New Venue 1"))
                .andExpect(jsonPath("$.venueCity").value("New Location"))
                .andExpect(jsonPath("$.venueAddress").value("New Address 1"))
                .andExpect(jsonPath("$.venueCapacity").value(2000));
    }

    @Test
    void findAll_returnsListOfVenues() throws Exception {
        Venue venue1 = new Venue();
        venue1.setName("Venue 2");
        venue1.setEmail("venue2@ironhack.com");
        venue1.setPassword("password2");
        venue1.setVenueName("Venue 2");
        venue1.setVenueCity("City 2");
        venue1.setVenueAddress("Address 2");
        venue1.setVenueCapacity(1500);
        venueRepository.save(venue1);

        Venue venue2 = new Venue();
        venue2.setName("Venue 3");
        venue2.setEmail("venue3@ironhack.com");
        venue2.setPassword("password3");
        venue2.setVenueName("Venue 3");
        venue2.setVenueCity("City 3");
        venue2.setVenueAddress("Address 3");
        venue2.setVenueCapacity(1200);
        venueRepository.save(venue2);

        mockMvc.perform(get("/api/venue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void findById_existingVenueId_venueFound() throws Exception {

        mockMvc.perform(get("/api/venue/{userId}", venue.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Venue 1"))
                .andExpect(jsonPath("$.email").value("venue@ironhack.com"));
    }

    @Test
    void findById_nonExistingVenueId_venueNotFound() throws Exception {
        mockMvc.perform(get("/api/venue/{userId}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingVenueId_venueUpdated() throws Exception {

        VenueUpdateRequest updateRequest = new VenueUpdateRequest();
        updateRequest.setName("Updated Venue");
        updateRequest.setVenueName("Updated Venue 1");
        updateRequest.setVenueAddress("Updated Address 1");
        updateRequest.setVenueCapacity(2500);

        String body = new ObjectMapper().writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/venue/{userId}/edit", venue.getUserId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Venue updatedVenue = venueRepository.findById(venue.getUserId()).orElseThrow(() ->
                new IllegalStateException("Venue not found"));

        assertEquals("Updated Venue", updatedVenue.getName());
        assertEquals("Updated Venue 1", updatedVenue.getVenueName());
        assertEquals("Updated Address 1", updatedVenue.getVenueAddress());
        assertEquals(2500, updatedVenue.getVenueCapacity());
    }

    @Test
    void delete_existingVenueId_venueDeleted() throws Exception {
        mockMvc.perform(delete("/api/venue/{userId}", venue.getUserId()))
                .andExpect(status().isNoContent());

        assertFalse(venueRepository.findById(venue.getUserId()).isPresent());
    }


}
