package org.ironhack.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironhack.project.models.enums.Genre;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistUpdateRequest {

    private String name;

    @Email(message = "Email should be valid.")
    @UniqueEmail(message = "This email is already registered.")
    private String email;

    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    private String artistName;

    private Genre genre;

}
