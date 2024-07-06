package org.ironhack.project.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConcertCreationRequest {

    @GeneratedValue
    private Integer concertId;

    @NotNull(message = "Concert name is mandatory.")
    private String concertName;

    @NotNull(message = "Artist is mandatory.")
    private Integer artistId;

    @NotNull(message = "Venue is mandatory-")
    private Integer venueId;

}
