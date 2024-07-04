package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Optional<Venue> findById(Integer id) {
        return venueRepository.findById(id);
    }

    public Venue update(Integer userId,
                        @Valid VenueUpdateRequest venueUpdateRequest) {

        Optional<Venue> optionalVenue = venueRepository.findById(userId);

        if (optionalVenue.isPresent()) {
            Venue venue = optionalVenue.get();

            if (venueUpdateRequest.getName() != null) {
                venue.setName(venueUpdateRequest.getName());
            }
            if (venueUpdateRequest.getEmail() != null) {
                venue.setEmail(venueUpdateRequest.getEmail());
            }
            if (venueUpdateRequest.getPassword() != null) {
                venue.setPassword(venueUpdateRequest.getPassword());
            }
            if (venueUpdateRequest.getVenueName() != null) {
                venue.setVenueName(venueUpdateRequest.getVenueName());
            }
            if (venueUpdateRequest.getVenueAddress() != null) {
                venue.setVenueAddress(venueUpdateRequest.getVenueAddress());
            }
            if (venueUpdateRequest.getVenueCity() != null) {
                venue.setVenueCity(venueUpdateRequest.getVenueCity());
            }
            if (venueUpdateRequest.getVenueCapacity() != null) {
                venue.setVenueCapacity(venueUpdateRequest.getVenueCapacity());
            }

            Venue updatedVenue = venueRepository.save(venue);
            return updatedVenue;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with this User Id.");
        }
    }

    public void deleteById(Integer userId) {
        Optional<Venue> optionalVenue = venueRepository.findById(userId);
        optionalVenue.ifPresentOrElse(
                venue -> venueRepository.delete(venue),
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with this User Id.");
                }
        );
    }

}
