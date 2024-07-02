package org.ironhack.project.services;

import org.ironhack.project.dtos.ArtistRequest;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ArtistServiceUnitTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_multipleArtists_foundAllArtists() {
        Artist artist1 = new Artist();
        artist1.setName("Artist A");
        artist1.setEmail("artista@ironhack.com");
        artist1.setPassword("password");

        Artist artist2 = new Artist();
        artist2.setName("Artist B");
        artist2.setEmail("artistb@ironhack.com");
        artist2.setPassword("password");

        when(artistRepository.findAll()).thenReturn(List.of(artist1, artist2));

        List<Artist> foundArtists = artistService.findAll();

        assertEquals(2, foundArtists.size());
        assertEquals("Artist A", foundArtists.get(0).getName());
        assertEquals("artista@ironhack.com", foundArtists.get(0).getEmail());
        assertEquals("Artist B", foundArtists.get(1).getName());
        assertEquals("artistb@ironhack.com", foundArtists.get(1).getEmail());
    }

    @Test
    void findById_existingArtistId_artistFound() {
        Integer userId = 1;
        Artist existingArtist = new Artist();
        existingArtist.setUserId(userId);
        existingArtist.setName("Found Artist");

        when(artistRepository.findById(userId)).thenReturn(Optional.of(existingArtist));

        Artist foundArtist = artistService.findById(userId).orElse(null);

        assertNotNull(foundArtist);
        assertEquals("Found Artist", foundArtist.getName());
    }

    @Test
    void findById_nonExistingArtistId_artistNotFound() {
        Integer userId = 1;

        when(artistRepository.findById(userId)).thenReturn(Optional.empty());

        Artist foundArtist = artistService.findById(userId).orElse(null);

        assertNull(foundArtist);
    }

    @Test
    void create_validArtistRequest_artistCreated() {
        ArtistRequest artistRequest = new ArtistRequest();
        artistRequest.setName("Artist A");
        artistRequest.setEmail("artista@ironhack.com");
        artistRequest.setPassword("password");
        artistRequest.setArtistName("Band A");
        artistRequest.setGenre(Genre.ROCK);

        Artist artistToSave = new Artist();
        artistToSave.setName(artistRequest.getName());
        artistToSave.setEmail(artistRequest.getEmail());
        artistToSave.setPassword(artistRequest.getPassword());
        artistToSave.setArtistName(artistRequest.getArtistName());
        artistToSave.setGenre(artistRequest.getGenre());

        when(artistRepository.save(any(Artist.class))).thenReturn(artistToSave);

        Artist savedArtist = artistService.create(artistRequest);

        assertNotNull(savedArtist);
        assertEquals("Artist A", savedArtist.getName());
        assertEquals("artista@ironhack.com", savedArtist.getEmail());
        assertEquals("password", savedArtist.getPassword());
        assertEquals("Band A", savedArtist.getArtistName());
        assertEquals(Genre.ROCK, savedArtist.getGenre());
    }

    @Test
    void update_existingArtistId_artistUpdated() {
        Integer userId = 1;
        ArtistUpdateRequest artistUpdateRequest = new ArtistUpdateRequest();
        artistUpdateRequest.setName("Updated Artist");
        artistUpdateRequest.setEmail("updated@ironhack.com");
        artistUpdateRequest.setPassword("password");
        artistUpdateRequest.setArtistName("UpdatedArtist");
        artistUpdateRequest.setGenre(Genre.POP);

        Artist existingArtist = new Artist();
        existingArtist.setUserId(userId);
        existingArtist.setName("Original Artist");
        existingArtist.setEmail("original@ironhack.com");
        existingArtist.setPassword("password");
        existingArtist.setArtistName("OriginalArtist");
        existingArtist.setGenre(Genre.ROCK);

        when(artistRepository.findById(userId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Artist updatedArtist = artistService.update(userId, artistUpdateRequest);

        assertNotNull(updatedArtist);
        assertEquals("Updated Artist", updatedArtist.getName());
        assertEquals("updated@ironhack.com", updatedArtist.getEmail());
        assertEquals("password", updatedArtist.getPassword());
        assertEquals("UpdatedArtist", updatedArtist.getArtistName());
        assertEquals(Genre.POP, updatedArtist.getGenre());
    }

    @Test
    void delete_existingArtistId_artistDeleted() {
        Artist artist = new Artist();
        artist.setUserId(1);

        when(artistRepository.findById(1)).thenReturn(Optional.of(artist));
        doNothing().when(artistRepository).delete(any(Artist.class));

        artistService.deleteById(1);

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).delete(any(Artist.class));
    }

    @Test
    void delete_nonExistingArtistId_artistNotFound() {
        when(artistRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> artistService.deleteById(1));
    }
}