package org.ironhack.project.services;

import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VenueServiceIntegrationTest {

    @Autowired
    private VenueService venueService;

    @Autowired
    private VenueRepository venueRepository;

    @AfterEach
    public void tearDown() {
        venueRepository.deleteAll();
    }

    @Test
    void findAll_multipleVenues_foundAllVenues() {
        Venue venue1 = new Venue();
        venue1.setName("Venue A");
        venue1.setEmail("venuea@ironhack.com");
        venue1.setPassword("password");
        venue1.setVenueName("VenueA");
        venue1.setVenueAddress("Address A");
        venue1.setVenueCity("City A");
        venue1.setVenueCapacity(100);
        venueRepository.save(venue1);

        Venue venue2 = new Venue();
        venue2.setName("Venue B");
        venue2.setEmail("venueb@ironhack.com");
        venue2.setPassword("password");
        venue2.setVenueName("VenueB");
        venue2.setVenueAddress("Address B");
        venue2.setVenueCity("City B");
        venue2.setVenueCapacity(200);
        venueRepository.save(venue2);

        List<Venue> foundVenues = venueService.findAll();

        assertEquals(2, foundVenues.size());
        assertEquals("Venue A", foundVenues.get(0).getName());
        assertEquals("venuea@ironhack.com", foundVenues.get(0).getEmail());
        assertEquals("Venue B", foundVenues.get(1).getName());
        assertEquals("venueb@ironhack.com", foundVenues.get(1).getEmail());
    }

    @Test
    void findById_existingVenueId_venueFound() {
        Venue venueToSave = new Venue();
        venueToSave.setName("Found Venue");
        venueToSave.setEmail("found@ironhack.com");
        venueToSave.setPassword("password");
        venueToSave.setVenueName("FoundVenue");
        venueToSave.setVenueAddress("Found Address");
        venueToSave.setVenueCity("Found City");
        venueToSave.setVenueCapacity(150);
        Venue savedVenue = venueRepository.save(venueToSave);

        Optional<Venue> foundVenue = venueService.findById(savedVenue.getUserId());

        assertTrue(foundVenue.isPresent());
        assertEquals("Found Venue", foundVenue.get().getName());
    }

    @Test
    void findById_nonExistingVenueId_venueNotFound() {
        Optional<Venue> foundVenue = venueService.findById(999);

        assertFalse(foundVenue.isPresent());
    }

    @Test
    void update_existingVenueId_venueUpdated() {
        Venue venueToSave = new Venue();
        venueToSave.setName("Original Venue");
        venueToSave.setEmail("original@ironhack.com");
        venueToSave.setPassword("password");
        venueToSave.setVenueName("OriginalVenue");
        venueToSave.setVenueAddress("Original Address");
        venueToSave.setVenueCity("Original City");
        venueToSave.setVenueCapacity(250);
        Venue savedVenue = venueRepository.save(venueToSave);

        VenueUpdateRequest venueUpdateRequest = new VenueUpdateRequest();
        venueUpdateRequest.setName("Updated Venue");
        venueUpdateRequest.setEmail("updated@ironhack.com");
        venueUpdateRequest.setVenueName("UpdatedVenue");
        venueUpdateRequest.setVenueAddress("Updated Address");
        venueUpdateRequest.setVenueCity("Updated City");
        venueUpdateRequest.setVenueCapacity(350);

        Venue updatedVenue = venueService.update(savedVenue.getUserId(), venueUpdateRequest);

        assertNotNull(updatedVenue);
        assertEquals("Updated Venue", updatedVenue.getName());
        assertEquals("updated@ironhack.com", updatedVenue.getEmail());
        assertEquals("UpdatedVenue", updatedVenue.getVenueName());
        assertEquals("Updated Address", updatedVenue.getVenueAddress());
        assertEquals("Updated City", updatedVenue.getVenueCity());
        assertEquals(350, updatedVenue.getVenueCapacity());
    }

    @Test
    void update_nonExistingVenueId_venueNotFound() {
        VenueUpdateRequest venueUpdateRequest = new VenueUpdateRequest();
        venueUpdateRequest.setName("Updated Venue");
        venueUpdateRequest.setEmail("updated@ironhack.com");

        assertThrows(ResponseStatusException.class, () -> venueService.update(0, venueUpdateRequest)); // Assuming ID 0 does not exist
    }

    @Test
    void delete_existingVenueId_venueDeleted() {
        Venue venueToSave = new Venue();
        venueToSave.setName("Venue to delete");
        venueToSave.setEmail("delete@ironhack.com");
        venueToSave.setPassword("password");
        venueToSave.setVenueName("VenueToDelete");
        venueToSave.setVenueAddress("Delete Address");
        venueToSave.setVenueCity("Delete City");
        venueToSave.setVenueCapacity(200);
        Venue savedVenue = venueRepository.save(venueToSave);

        venueService.deleteById(savedVenue.getUserId());

        assertFalse(venueRepository.findById(savedVenue.getUserId()).isPresent());
    }

    @Test
    void delete_nonExistingVenueId_venueNotFound() {
        assertThrows(ResponseStatusException.class, () -> venueService.deleteById(0)); // Assuming ID 0 does not exist
    }
}
