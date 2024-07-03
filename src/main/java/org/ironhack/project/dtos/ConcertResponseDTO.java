package org.ironhack.project.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcertResponseDTO {

    private Integer concertId;
    private String concertName;

    private ArtistDTO artist;
    private VenueDTO venue;

}
