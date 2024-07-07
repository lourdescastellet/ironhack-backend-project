package org.ironhack.project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateRequest {

    private String name;

    @Email(message = "Email should be valid")
    @UniqueEmail(message = "This email is already registered.")
    private String email;

    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;

}
