package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.ironhack.project.models.enums.TicketType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert extends Event{

    private String concertName;

    @ManyToOne
    private Artist artist;

    @ManyToOne
    private Venue venue;

    @OneToMany
    private Set<Ticket> tickets = new HashSet<>();

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<TicketType,Integer> ticketAllowance = new HashMap<>();


}
