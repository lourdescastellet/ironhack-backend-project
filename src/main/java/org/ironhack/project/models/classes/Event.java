package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@MappedSuperclass
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer eventId;

    private LocalDate eventDate;

}
