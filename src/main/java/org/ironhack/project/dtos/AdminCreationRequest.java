package org.ironhack.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ironhack.project.validators.UniqueEmail;

@Data
public class AdminCreationRequest {

    @NotEmpty(message = "Name can't be empty or null.")
    private String name;

    @NotEmpty(message = "Email can't be empty or null.")
    @Email(message = "Email should be valid")
    @UniqueEmail(message = "This email is already registered.")
    private String email;

    @NotEmpty(message = "Password can't be empty or null.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;
}
