package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    boolean existsByEmail(String email);

    Artist findByEmail(String email);

    @Query("SELECT a FROM Artist a WHERE a.genre = ?1")
    List<Artist> findByGenre(Genre genre);
}
