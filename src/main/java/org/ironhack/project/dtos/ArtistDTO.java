package org.ironhack.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ironhack.project.models.enums.Genre;

@Getter
@Setter
public class ArtistDTO {

    @NotEmpty(message = "Username can't be empty or null.")
    private String userName;

    @NotEmpty(message = "Name can't be empty or null.")
    private String name;

    @NotEmpty(message = "Email can't be empty or null.")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password can't be empty or null.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    @NotEmpty(message = "Artist name can't be empty or null.")
    private String artistName;

    private Genre genre;
}
