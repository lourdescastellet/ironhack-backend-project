package org.ironhack.project.models.classes;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ironhack.project.models.enums.Role;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Customer extends User {

    private String paymentMethod;

    private String customerAddress;

    // Set a predefined role
    public Customer() {
        this.roles = Set.of(Role.CUSTOMER);
    }
}