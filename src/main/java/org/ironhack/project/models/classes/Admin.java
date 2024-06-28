package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ironhack.project.models.enums.Role;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Admin extends User {

    // Set a predefined role
    public Admin() {
        this.setRoles(Set.of(Role.ADMIN));
    }

}
