package org.ironhack.project.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.ironhack.project.models.enums.Genre;

@Data
public class RegistrationRequest {

    // Admin + all users
    @NotEmpty(message = "Name can't be empty or null.")
    private String name;

    @NotEmpty(message = "Email can't be empty or null.")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password can't be empty or null.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

    // Artist
    @NotEmpty(message = "Artist name can't be empty or null.")
    private String artistName;

    @NotNull(message = "Genre can't be null.")
    private Genre genre;

    // Customer
    @NotEmpty(message = "Payment method can't be empty or null.")
    private String paymentMethod;

    @NotEmpty(message = "Address can't be empty or null.")
    private String customerAddress;

    // Venue
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
