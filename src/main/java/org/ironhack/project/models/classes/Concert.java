package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.ironhack.project.models.enums.TicketType;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concertId;

    private String concertName;

    @ManyToOne
    private Artist artist;

    @ManyToOne
    private Venue venue;

    @ManyToOne
    private Admin admin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Ticket> tickets = new HashSet<>();

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<TicketType,Integer> ticketAllowance = new HashMap<>();

    public void setVenue(Venue venue) {
        this.venue = venue;
        initializeTicketAllowance();
    }

    private void initializeTicketAllowance() {
        if (this.venue != null) {
            int capacity = this.venue.getVenueCapacity();
            for (TicketType type : TicketType.values()) {
                int allowance = (int) (capacity * type.getAllowancePercentage());
                ticketAllowance.put(type, allowance);
            }
        }
    }

    public boolean canAddTicket(TicketType ticketType) {
        int currentCount = (int) tickets.stream()
                .filter(ticket -> ticket.getTicketType() == ticketType)
                .count();
        int allowedCount = ticketAllowance.getOrDefault(ticketType, 0);
        return currentCount < allowedCount;
    }

    public void addTicket(Ticket ticket) {
        if (canAddTicket(ticket.getTicketType())) {
            ticket.setConcert(this);
            tickets.add(ticket);
        } else {
            throw new IllegalArgumentException("Cannot add ticket. Allowance exceeded for type: " + ticket.getTicketType());
        }
    }


}
