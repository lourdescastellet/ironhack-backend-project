package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ironhack.project.models.enums.Role;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;

@MappedSuperclass
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Integer userId;

    private String name;


    private String email;

    private String password;

    @ElementCollection(fetch = EAGER)
    @Enumerated(EnumType.STRING)
    protected Set<Role> roles;

}
