package org.ironhack.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {

    private String venueName;
    private String venueAddress;
    private String venueCity;
    private Integer venueCapacity;


}
