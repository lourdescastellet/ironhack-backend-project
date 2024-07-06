package org.ironhack.project.dtos;

import lombok.*;
import org.ironhack.project.models.enums.Genre;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {

    private String artistName;
    private Genre genre;

    public ArtistDTO(String artistName) {
    }
}
