package org.ironhack.project.services;

import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class VenueServiceUnitTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        venueRepository.deleteAll();
    }

    @Test
    void findById_existingVenueId_venueFound() {
        Integer userId = 1;
        Venue existingVenue = new Venue();
        existingVenue.setUserId(userId);
        existingVenue.setVenueName("Found Venue");
        existingVenue.setVenueCity("City A");

        when(venueRepository.findById(userId)).thenReturn(Optional.of(existingVenue));

        VenueDTO foundVenue = venueService.findById(userId).orElse(null);

        assertNotNull(foundVenue);
        assertEquals("Found Venue", foundVenue.getVenueName());
        assertEquals("City A", foundVenue.getVenueCity());
    }

    @Test
    void findById_nonExistingVenueId_venueNotFound() {
        Integer userId = 1;

        when(venueRepository.findById(userId)).thenReturn(Optional.empty());

        VenueDTO foundVenue = venueService.findById(userId).orElse(null);

        assertNull(foundVenue);
    }

    @Test
    void update_existingVenueId_venueUpdated() {
        Integer userId = 1;
        VenueUpdateRequest updateRequest = new VenueUpdateRequest();
        updateRequest.setName("Updated Venue");
        updateRequest.setVenueAddress("Updated Address");
        updateRequest.setVenueCapacity(150);

        Venue existingVenue = new Venue();
        existingVenue.setUserId(userId);
        existingVenue.setName("Original Venue");
        existingVenue.setEmail("original@ironhack.com");
        existingVenue.setPassword("password");
        existingVenue.setVenueName("Original Venue");
        existingVenue.setVenueAddress("Original Address");
        existingVenue.setVenueCity("Original City");
        existingVenue.setVenueCapacity(100);

        when(venueRepository.findById(userId)).thenReturn(Optional.of(existingVenue));
        when(venueRepository.save(any(Venue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venue updatedVenue = venueService.update(userId, updateRequest);

        assertNotNull(updatedVenue);
        assertEquals("Updated Venue", updatedVenue.getName());
        assertEquals("original@ironhack.com", updatedVenue.getEmail());
        assertEquals("Original Venue", updatedVenue.getVenueName());
        assertEquals("Updated Address", updatedVenue.getVenueAddress());
        assertEquals("Original City", updatedVenue.getVenueCity()); // Ensure unchanged fields remain unchanged
        assertEquals(150, updatedVenue.getVenueCapacity());
    }

    @Test
    void delete_existingVenueId_venueDeleted() {
        Venue venue = new Venue();
        Integer userId = 1;
        venue.setUserId(userId);

        when(venueRepository.findById(userId)).thenReturn(Optional.of(venue));
        doNothing().when(venueRepository).delete(any(Venue.class));

        venueService.deleteById(userId);

        verify(venueRepository, times(1)).findById(userId);
        verify(venueRepository, times(1)).delete(any(Venue.class));
    }

    @Test
    void delete_nonExistingVenueId_venueNotFound() {

        when(venueRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> venueService.deleteById(1));
    }
}
