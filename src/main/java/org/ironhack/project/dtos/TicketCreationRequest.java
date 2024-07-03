package org.ironhack.project.dtos;

import jakarta.validation.constraints.NotNull;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;

import java.math.BigDecimal;

public class TicketCreationRequest {

    private Integer ticketId;

    @NotNull(message = "Ticket price is required")
    private BigDecimal ticketPrice;

    @NotNull(message = "Ticket type is required")
    private TicketType ticketType;

    private Status status;

    private Integer concertId;
}
