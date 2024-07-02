package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArtistRepositoryIntegrationTest {

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    void setUp() {
        Artist artist1 = new Artist();
        artist1.setName("Artist A");
        artist1.setEmail("artista@ironhack.com");
        artist1.setPassword("password");
        artist1.setGenre(Genre.POP);
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Artist B");
        artist2.setEmail("artistb@ironhack.com");
        artist2.setPassword("password");
        artist2.setGenre(Genre.ROCK);
        artistRepository.save(artist2);
    }

    @AfterEach
    void tearDown() {
        artistRepository.deleteAll();
    }

    @Test
    void saveArtist_newArtist_artistSaved() {
        Artist artist = new Artist();
        artist.setName("Artist B");
        artist.setEmail("artistb@ironhack.com");
        artist.setPassword("password");
        Artist savedArtist = artistRepository.save(artist);
        assertNotNull(savedArtist);
        assertEquals("Artist B", savedArtist.getName());
        assertEquals("artistb@ironhack.com", savedArtist.getEmail());
        assertEquals("password", savedArtist.getPassword());
    }

    @Test
    void findByEmail_existingEmail_artistReturned() {
        Artist found = artistRepository.findByEmail("artista@ironhack.com");
        assertNotNull(found);
        assertEquals("Artist A", found.getName());
        assertEquals("artista@ironhack.com", found.getEmail());
    }

    @Test
    void findByEmail_nonExistingEmail_nullReturned() {
        Artist found = artistRepository.findByEmail("nonexisting@ironhack.com");
        assertNull(found);
    }

    @Test
    void findByGenre_existingGenre_artistsReturned() {
        List<Artist> popArtists = artistRepository.findByGenre(Genre.POP);
        assertNotNull(popArtists);
        assertEquals(1, popArtists.size());
        Artist foundArtist = popArtists.get(0);
        assertEquals("Artist A", foundArtist.getName());
        assertEquals(Genre.POP, foundArtist.getGenre());
        assertEquals("artista@ironhack.com", foundArtist.getEmail());
        assertEquals("password", foundArtist.getPassword()); // Assert password is saved correctly
    }

    @Test
    void findByGenre_nonExistingGenre_emptyListReturned() {
        List<Artist> jazzArtists = artistRepository.findByGenre(Genre.JAZZ);
        assertNotNull(jazzArtists);
        assertTrue(jazzArtists.isEmpty());
    }


}