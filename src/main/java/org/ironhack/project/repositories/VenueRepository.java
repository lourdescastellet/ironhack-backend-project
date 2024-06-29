package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

    Venue findByEmail(String email);

    Venue findByVenueName(String venueName);

    List<Venue> findByVenueCity(String venueCity);

    List<Venue> findByVenueCapacityGreaterThan(int capacity);

    List<Venue> findByVenueCapacityLessThan(int capacity);


}
