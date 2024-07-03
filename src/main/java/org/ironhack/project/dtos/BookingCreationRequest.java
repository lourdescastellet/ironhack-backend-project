package org.ironhack.project.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingCreationRequest {

    private Integer bookingId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Ticket ID is required")
    private Integer ticketId;

    @NotNull(message = "Concert ID is required")
    private Integer concertId;

    private LocalDate eventDate;

}
