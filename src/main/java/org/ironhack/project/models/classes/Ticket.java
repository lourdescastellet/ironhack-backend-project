package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends Event{

    private BigDecimal ticketPrice;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Concert concert;

}
