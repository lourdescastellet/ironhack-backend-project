package org.ironhack.project.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ironhack.project.models.classes.UniqueEmail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueUpdateRequest {

    private String name;

    @Email(message = "Email should be valid.")
    @UniqueEmail(message = "This email is already registered.")
    private String email;

    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    private String venueName;

    private String venueAddress;

    private String venueCity;

    @Min(value = 50, message = "Venue capacity must be at least 50.")
    private Integer venueCapacity;
}
