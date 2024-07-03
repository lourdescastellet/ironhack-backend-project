package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer eventId;

    private LocalDate eventDate;

}
