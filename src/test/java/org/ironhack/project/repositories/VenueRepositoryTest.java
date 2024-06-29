package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Venue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @BeforeEach
    void setUp() {
        Venue venue1 = new Venue();
        venue1.setName("Venue A");
        venue1.setEmail("venuea@ironhack.com");
        venue1.setPassword("password");
        venue1.setVenueName("VENUE_A");
        venue1.setVenueCity("City A");
        venue1.setVenueCapacity(100);
        venueRepository.save(venue1);

        Venue venue2 = new Venue();
        venue2.setName("Venue B");
        venue2.setEmail("venueb@ironhack.com");
        venue2.setPassword("password");
        venue2.setVenueName("VENUE_B");
        venue2.setVenueCity("City B");
        venue2.setVenueCapacity(200);
        venueRepository.save(venue2);
    }

    @AfterEach
    void tearDown() {
        venueRepository.deleteAll();
    }

    @Test
    void saveVenue_newVenue_venueSaved() {
        Venue venue = new Venue();
        venue.setName("Venue B");
        venue.setEmail("venueb@ironhack.com");
        venue.setPassword("password");
        Venue savedVenue = venueRepository.save(venue);
        assertNotNull(savedVenue);
        assertEquals("Venue B", savedVenue.getName());
        assertEquals("venueb@ironhack.com", savedVenue.getEmail());
        assertEquals("password", savedVenue.getPassword());
    }

    @Test
    void findByEmail_existingEmail_venueReturned() {
        Venue found = venueRepository.findByEmail("venuea@ironhack.com");
        assertNotNull(found);
        assertEquals("Venue A", found.getName());
        assertEquals("venuea@ironhack.com", found.getEmail());
    }

    @Test
    void findByEmail_nonExistingEmail_nullReturned() {
        Venue found = venueRepository.findByEmail("nonexisting@ironhack.com");
        assertNull(found);
    }

    @Test
    void findByVenueName_existingVenueName_venueReturned() {
        Venue found = venueRepository.findByVenueName("VENUE_A");
        assertNotNull(found);
        assertEquals("Venue A", found.getName());
        assertEquals("VENUE_A", found.getVenueName());
    }

    @Test
    void findByVenueCity_existingVenueCity_venuesReturned() {
        List<Venue> foundVenues = venueRepository.findByVenueCity("City B");
        assertNotNull(foundVenues);
        assertEquals(1, foundVenues.size());
        Venue foundVenue = foundVenues.get(0);
        assertEquals("Venue B", foundVenue.getName());
        assertEquals("City B", foundVenue.getVenueCity());
    }

    @Test
    void findByVenueCapacityGreaterThan_venuesWithGreaterCapacityReturned() {
        List<Venue> foundVenues = venueRepository.findByVenueCapacityGreaterThan(150);
        assertNotNull(foundVenues);
        assertEquals(1, foundVenues.size());
        Venue foundVenue = foundVenues.get(0);
        assertEquals("Venue B", foundVenue.getName());
        assertTrue(foundVenue.getVenueCapacity() > 150);
    }

    @Test
    void findByVenueCapacityLessThan_venuesWithLesserCapacityReturned() {
        List<Venue> foundVenues = venueRepository.findByVenueCapacityLessThan(150);
        assertNotNull(foundVenues);
        assertEquals(1, foundVenues.size());
        Venue foundVenue = foundVenues.get(0);
        assertEquals("Venue A", foundVenue.getName());
        assertTrue(foundVenue.getVenueCapacity() < 150);
    }
}