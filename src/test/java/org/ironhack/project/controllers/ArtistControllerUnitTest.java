package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.ArtistRequest;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.ironhack.project.services.ArtistService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtistControllerUnitTest {

    @InjectMocks
    private ArtistController artistController;

    @Mock
    private ArtistService artistService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }

    @Test
    void findById_existingId_artistReturned() throws Exception {
        Artist artist = new Artist();
        artist.setUserId(1);
        artist.setName("Artist A");

        when(artistService.findById(1)).thenReturn(Optional.of(artist));

        mockMvc.perform(get("/api/artist/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Artist A"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(artistService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artist/{userId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_Artist_validArtist_artistCreated() throws Exception {
        ArtistRequest artistRequest = new ArtistRequest();
        artistRequest.setName("Artist A");
        artistRequest.setEmail("artista@ironhack.com");
        artistRequest.setPassword("password");
        artistRequest.setArtistName("Art-A");
        artistRequest.setGenre(Genre.JAZZ);

        Artist artist = new Artist();
        artist.setName(artistRequest.getName());
        artist.setEmail(artistRequest.getEmail());
        artist.setPassword(artistRequest.getPassword());
        artist.setArtistName(artistRequest.getArtistName());
        artist.setGenre(artistRequest.getGenre());

        when(artistService.create(any(ArtistRequest.class))).thenReturn(artist);

        mockMvc.perform(post("/api/artist/new")
                        .content(objectMapper.writeValueAsString(artistRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Artist A"));
    }

    @Test
    void update_existingId_artistUpdated() throws Exception {
        ArtistUpdateRequest artistUpdateRequest = new ArtistUpdateRequest();
        artistUpdateRequest.setName("Updated Artist");
        artistUpdateRequest.setEmail("updated@ironhack.com");
        artistUpdateRequest.setPassword("newpassword");

        Artist artist = new Artist();
        artist.setUserId(1);
        artist.setName(artistUpdateRequest.getName());
        artist.setEmail(artistUpdateRequest.getEmail());
        artist.setPassword(artistUpdateRequest.getPassword());

        when(artistService.update(anyInt(), any(ArtistUpdateRequest.class))).thenReturn(artist);

        mockMvc.perform(put("/api/artist/{userId}/edit", 1)
                        .content(objectMapper.writeValueAsString(artistUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_artistDeleted() throws Exception {
        doNothing().when(artistService).deleteById(anyInt());

        mockMvc.perform(delete("/api/artist/{userId}", 1))
                .andExpect(status().isNoContent());
    }

}