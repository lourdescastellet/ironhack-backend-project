package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.ArtistCreationRequest;
import org.ironhack.project.dtos.ArtistUpdateRequest;
import org.ironhack.project.models.classes.Artist;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.repositories.ArtistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ArtistControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArtistRepository artistRepository;

    private MockMvc mockMvc;
    private Artist artist;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        artist = new Artist();
        artist.setName("Artist");
        artist.setEmail("artist@ironhack.com");
        artist.setPassword("password");
        artist.setArtistName("ArtistA");
        artist.setGenre(Genre.EDM);
        artist = artistRepository.save(artist);
    }

    @AfterEach
    public void tearDown() {
        artistRepository.deleteAll();
    }

    @Test
    void createArtist_createsArtist() throws Exception {
        ArtistCreationRequest newArtist = new ArtistCreationRequest();
        newArtist.setName("New Artist");
        newArtist.setEmail("newartist@ironhack.com");
        newArtist.setPassword("password");
        newArtist.setArtistName("NewArtist");
        newArtist.setGenre(Genre.JAZZ);

        String body = new ObjectMapper().writeValueAsString(newArtist);

        mockMvc.perform(post("/api/artist/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Artist"));
    }

    @Test
    void findAll_returnsListOfArtists() throws Exception {
        Artist artist1 = new Artist();
        artist1.setName("Artist 1");
        artist1.setGenre(Genre.HIPHOP);
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Artist 2");
        artist2.setGenre(Genre.POP);
        artistRepository.save(artist2);

        mockMvc.perform(get("/api/artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void findById_existingArtistId_artistFound() throws Exception {
        Artist artist = new Artist();
        artist.setName("Artist");
        artist.setGenre(Genre.ROCK);
        artist = artistRepository.save(artist);

        mockMvc.perform(get("/api/artist/{userId}", artist.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Artist"))
                .andExpect(jsonPath("$.genre").value(Genre.ROCK.toString()));
    }

    @Test
    void findById_nonExistingArtistId_artistNotFound() throws Exception {
        mockMvc.perform(get("/api/artist/{userId}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingArtistId_artistUpdated() throws Exception {
        ArtistUpdateRequest updateRequest = new ArtistUpdateRequest();
        updateRequest.setName("Updated Artist");
        updateRequest.setGenre(Genre.RNB);

        String body = new ObjectMapper().writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/artist/{userId}/edit", artist.getUserId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Artist updated = artistRepository.findById(artist.getUserId()).orElseThrow(() ->
                new IllegalStateException("Artist not found"));

        assertEquals("Updated Artist", updated.getName());
        assertEquals(Genre.RNB, updated.getGenre());
    }
    @Test
    void delete_existingArtistId_artistDeleted() throws Exception {

        mockMvc.perform(delete("/api/artist/{userId}", artist.getUserId()))
                .andExpect(status().isNoContent());

        assertFalse(artistRepository.findById(artist.getUserId()).isPresent());
    }











}
