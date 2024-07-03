package org.ironhack.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.project.dtos.ConcertCreationRequest;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.services.ConcertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class ConcertControllerUnitTest {

    @InjectMocks
    private ConcertController concertController;

    @Mock
    private ConcertService concertService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(concertController).build();
    }

    @Test
    void findAll_concertsFound() throws Exception {
        Concert concert1 = new Concert();
        concert1.setConcertId(1);
        concert1.setConcertName("Concert 1");

        Concert concert2 = new Concert();
        concert2.setConcertId(2);
        concert2.setConcertName("Concert 2");

        when(concertService.findAllConcerts()).thenReturn(List.of(concert1, concert2));

        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].concertId").value(1))
                .andExpect(jsonPath("$[0].concertName").value("Concert 1"))
                .andExpect(jsonPath("$[1].concertId").value(2))
                .andExpect(jsonPath("$[1].concertName").value("Concert 2"));
    }

    @Test
    void findById_existingId_concertReturned() throws Exception {
        Concert concert = new Concert();
        concert.setConcertId(1);
        concert.setConcertName("Concert A");

        when(concertService.findConcertById(1)).thenReturn(Optional.of(concert));

        mockMvc.perform(get("/api/concerts/{concertId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertId").value(1))
                .andExpect(jsonPath("$.concertName").value("Concert A"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(concertService.findConcertById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/concerts/{concertId}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_validConcert_concertCreated() throws Exception {
        ConcertCreationRequest concertCreationRequest = new ConcertCreationRequest();
        concertCreationRequest.setConcertName("Concert A");
        concertCreationRequest.setArtistId(1);
        concertCreationRequest.setVenueId(1);

        Concert concertToSave = new Concert();
        concertToSave.setConcertId(1);
        concertToSave.setConcertName(concertCreationRequest.getConcertName());

        when(concertService.createConcert(any())).thenReturn(concertToSave);

        mockMvc.perform(post("/api/concerts/new")
                        .content(objectMapper.writeValueAsString(concertCreationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.concertName").value("Concert A"));
    }

    @Test
    void delete_existingConcert_concertDeleted() throws Exception {
        Concert concertToDelete = new Concert();
        concertToDelete.setConcertId(1);

        when(concertService.findConcertById(1)).thenReturn(Optional.of(concertToDelete));
        doNothing().when(concertService).deleteConcert(1);

        mockMvc.perform(delete("/api/concerts/{concertId}", 1))
                .andExpect(status().isNoContent());

        verify(concertService, times(1)).deleteConcert(1);
    }

    @Test
    void delete_nonExistingConcert_concertNotFound() throws Exception {
        when(concertService.findConcertById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/concerts/{concertId}", 1))
                .andExpect(status().isNoContent());
    }
}