package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findByEmail(String email);
}
