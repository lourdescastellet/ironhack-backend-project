package org.ironhack.project.dtos;

import lombok.Getter;
import lombok.Setter;
import org.ironhack.project.models.enums.Genre;

@Getter
@Setter
public class ArtistDTO {

    private String artistName;
    private Genre genre;

}
