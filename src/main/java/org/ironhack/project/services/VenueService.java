package org.ironhack.project.services;

import jakarta.validation.Valid;
import org.ironhack.project.dtos.VenueDTO;
import org.ironhack.project.dtos.VenueUpdateRequest;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<VenueDTO> findAll() {
        return venueRepository.findAll().stream()
                .map(venue -> {
                    VenueDTO venueDTO = new VenueDTO();
                    venueDTO.setVenueName(venue.getVenueName());
                    venueDTO.setVenueAddress(venue.getVenueAddress());
                    venueDTO.setVenueCity(venue.getVenueCity());
                    venueDTO.setVenueCapacity(venue.getVenueCapacity());
                    return venueDTO;
                }).collect(Collectors.toList());
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
                venue.setPassword(passwordEncoder.encode(venueUpdateRequest.getPassword()));
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
                // TODO run ticketAllowance again to adapt to new capacity
                venue.setVenueCapacity(venueUpdateRequest.getVenueCapacity());
            }

            Venue updatedVenue = venueRepository.save(venue);
            System.out.println("Updated venue: " + updatedVenue);
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
