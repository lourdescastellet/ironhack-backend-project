package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ironhack.project.models.enums.Genre;
import org.ironhack.project.models.enums.Role;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
public class Artist extends User{

    private String artistName;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    // Profit

    private BigDecimal profitPercentage = BigDecimal.ZERO;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Set a predefined role
    public Artist() {
        this.roles = Set.of(Role.ARTIST);
    }
}


