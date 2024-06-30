package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueRequest;
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

    public Venue create(@Valid VenueRequest venueRequest) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getName());
        venue.setEmail(venueRequest.getEmail());
        venue.setPassword(venueRequest.getPassword());
        venue.setVenueName(venueRequest.getVenueName());
        venue.setVenueAddress(venueRequest.getVenueAddress());
        venue.setVenueCity(venueRequest.getVenueCity());
        venue.setVenueCapacity(venueRequest.getVenueCapacity());

        return venueRepository.save(venue);
    }

    public Venue update(Integer userId, @Valid VenueUpdateRequest venueUpdateRequest) {

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

            venueRepository.save(venue);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with this User Id.");
        }
        return null;
    }


    public void deleteById(Integer userId) {
        venueRepository.deleteById(userId);
    }
}
