package org.ironhack.project.services;

import org.ironhack.project.dtos.ArtistDTO;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.ArtistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArtistServiceIntegrationTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @AfterEach
    public void tearDown() {
        artistRepository.deleteAll();
    }

    @Test
    void findAll_multipleArtists_foundAllArtists() {
        Artist artist1 = new Artist();
        artist1.setName("Artist A");
        artist1.setEmail("artista@ironhack.com");
        artist1.setPassword("password");
        artist1.setArtistName("ArtistA");
        artist1.setGenre(Genre.ROCK);
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Artist B");
        artist2.setEmail("artistb@ironhack.com");
        artist2.setPassword("password");
        artist2.setArtistName("ArtistB");
        artist2.setGenre(Genre.POP);
        artistRepository.save(artist2);

        List<ArtistDTO> foundArtists = artistService.findAll();

        assertEquals(2, foundArtists.size());
        assertEquals("ArtistA", foundArtists.get(0).getArtistName());
        assertEquals(Genre.ROCK, foundArtists.get(0).getGenre());
        assertEquals("ArtistB", foundArtists.get(1).getArtistName());
        assertEquals(Genre.POP, foundArtists.get(1).getGenre());
    }

    @Test
    void findById_existingArtistId_artistFound() {
        Artist artistToSave = new Artist();
        artistToSave.setArtistName("FoundArtist");
        artistToSave.setGenre(Genre.COUNTRY);
        Artist savedArtist = artistRepository.save(artistToSave);

        Optional<ArtistDTO> foundArtist = artistService.findById(savedArtist.getUserId());

        assertTrue(foundArtist.isPresent());
        assertEquals("FoundArtist", foundArtist.get().getArtistName());
    }

    @Test
    void findById_nonExistingArtistId_artistNotFound() {
        Optional<ArtistDTO> foundArtist = artistService.findById(0); // Assuming ID 0 does not exist

        assertFalse(foundArtist.isPresent());
    }

    @Test
    void update_existingArtistId_artistUpdated() {
        Artist artistToSave = new Artist();
        artistToSave.setName("Original Artist");
        artistToSave.setEmail("original@ironhack.com");
        artistToSave.setPassword("password");
        artistToSave.setArtistName("OriginalArtist");
        artistToSave.setGenre(Genre.COUNTRY);
        Artist savedArtist = artistRepository.save(artistToSave);

        ArtistUpdateRequest artistUpdateRequest = new ArtistUpdateRequest();
        artistUpdateRequest.setName("Updated Artist");
        artistUpdateRequest.setEmail("updated@ironhack.com");
        artistUpdateRequest.setArtistName("UpdatedArtist");
        artistUpdateRequest.setGenre(Genre.JAZZ);

        Artist updatedArtist = artistService.update(savedArtist.getUserId(), artistUpdateRequest);

        assertNotNull(updatedArtist);
        assertEquals("Updated Artist", updatedArtist.getName());
        assertEquals("updated@ironhack.com", updatedArtist.getEmail());
        assertEquals("UpdatedArtist", updatedArtist.getArtistName());
        assertEquals(Genre.JAZZ, updatedArtist.getGenre());
    }

    @Test
    void update_nonExistingArtistId_artistNotFound() {
        ArtistUpdateRequest artistUpdateRequest = new ArtistUpdateRequest();
        artistUpdateRequest.setName("Updated Artist");
        artistUpdateRequest.setEmail("updated@ironhack.com");

        assertThrows(ResponseStatusException.class, () -> artistService.update(0, artistUpdateRequest)); // Assuming ID 0 does not exist
    }

    @Test
    void delete_existingArtistId_artistDeleted() {
        Artist artistToSave = new Artist();
        artistToSave.setName("Artist to delete");
        artistToSave.setEmail("delete@ironhack.com");
        artistToSave.setPassword("password");
        artistToSave.setArtistName("ArtistToDelete");
        artistToSave.setGenre(Genre.EDM);
        Artist savedArtist = artistRepository.save(artistToSave);

        artistService.deleteById(savedArtist.getUserId());

        assertFalse(artistRepository.findById(savedArtist.getUserId()).isPresent());
    }

    @Test
    void delete_nonExistingArtistId_artistNotFound() {
        assertThrows(ResponseStatusException.class, () -> artistService.deleteById(0)); // Assuming ID 0 does not exist
    }
}
