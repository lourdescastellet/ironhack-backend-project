package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.ironhack.project.models.enums.Role;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
public class Venue extends User{

    private String venueName;
    private String venueAddress;
    private String venueCity;
    private int venueCapacity;

    // Profit
    private BigDecimal profitPercentage = BigDecimal.ZERO;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Set a predefined role
    public Venue() {
        this.roles = Set.of(Role.VENUE);
    }

}
