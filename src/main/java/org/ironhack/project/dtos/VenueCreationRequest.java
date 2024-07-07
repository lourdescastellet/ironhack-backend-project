package org.ironhack.project.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VenueCreationRequest {

    @NotEmpty(message = "Name can't be empty or null.")
    private String name;

    @NotEmpty(message = "Email can't be empty or null.")
    @Email(message = "Email should be valid")
    @UniqueEmail(message = "This email is already registered.")
    private String email;

    @NotEmpty(message = "Password can't be empty or null.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    @NotEmpty(message = "Venue name can't be empty or null.")
    private String venueName;

    @NotEmpty(message = "Venue address can't be empty or null.")
    private String venueAddress;

    @NotEmpty(message = "Venue city can't be empty or null.")
    private String venueCity;

    @NotNull(message = "Venue capacity can't be null.")
    @Min(value = 50, message = "Venue capacity must be at least 50.")
    private Integer venueCapacity;

}
