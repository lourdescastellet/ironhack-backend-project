package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
}
