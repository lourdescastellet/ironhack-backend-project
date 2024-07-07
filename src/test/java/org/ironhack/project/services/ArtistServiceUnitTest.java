package org.ironhack.project.services;

import org.ironhack.project.dtos.ArtistDTO;
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
        artist1.setArtistName("Artist A");
        artist1.setGenre(Genre.EDM);


        Artist artist2 = new Artist();
        artist2.setArtistName("Artist B");
        artist2.setGenre(Genre.JAZZ);


        when(artistRepository.findAll()).thenReturn(List.of(artist1, artist2));

        List<ArtistDTO> foundArtists = artistService.findAll();

        assertEquals(2, foundArtists.size());
        assertEquals("Artist A", foundArtists.get(0).getArtistName());
        assertEquals(Genre.EDM, foundArtists.get(0).getGenre());
        assertEquals("Artist B", foundArtists.get(1).getArtistName());
        assertEquals(Genre.JAZZ, foundArtists.get(1).getGenre());
    }

    @Test
    void findById_existingArtistId_artistFound() {
        Integer userId = 1;
        Artist existingArtist = new Artist();
        existingArtist.setUserId(userId);
        existingArtist.setArtistName("Found Artist");

        when(artistRepository.findById(userId)).thenReturn(Optional.of(existingArtist));

        ArtistDTO foundArtist = artistService.findById(userId).orElse(null);

        assertNotNull(foundArtist);
        assertEquals("Found Artist", foundArtist.getArtistName());
    }

    @Test
    void findById_nonExistingArtistId_artistNotFound() {
        Integer userId = 1;

        when(artistRepository.findById(userId)).thenReturn(Optional.empty());

        ArtistDTO foundArtist = artistService.findById(userId).orElse(null);

        assertNull(foundArtist);
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