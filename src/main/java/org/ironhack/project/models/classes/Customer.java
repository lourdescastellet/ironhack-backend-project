package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ironhack.project.models.enums.Role;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {

    private String paymentMethod;

    private String customerAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Set a predefined role
    public Customer() {
        this.roles = Set.of(Role.CUSTOMER);
    }
}