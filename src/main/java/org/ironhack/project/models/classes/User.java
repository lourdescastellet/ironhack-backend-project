package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.*;
import org.ironhack.project.models.enums.Role;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;

@MappedSuperclass
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;

    private String name;

    private String email;

    private String password;

    @ElementCollection(fetch = EAGER)
    @Enumerated(EnumType.STRING)
    protected Set<Role> roles;


}
