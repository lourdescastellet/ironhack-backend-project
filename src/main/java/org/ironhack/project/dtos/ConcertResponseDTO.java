package org.ironhack.project.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironhack.project.models.enums.TicketType;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponseDTO {

    private Integer concertId;
    private String concertName;
    private ArtistDTO artist;
    private VenueDTO venue;
    private Map<TicketType, Integer> ticketAllowance;

}
