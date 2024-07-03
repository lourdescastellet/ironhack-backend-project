package org.ironhack.project.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConcertCreationRequest {

    private Integer concertId;

    private String concertName;

    @NotNull(message = "Artist is mandatory.")
    private Integer artistId;

    @NotNull(message = "Venue is mandatory-")
    private Integer venueId;

}
