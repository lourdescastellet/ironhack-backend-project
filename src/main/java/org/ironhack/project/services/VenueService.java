package org.ironhack.project.services;

import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Venue create(Venue venue) {
        return venueRepository.save(venue);
    }

    public void deleteById(Integer id) {
        venueRepository.deleteById(id);
    }
}
