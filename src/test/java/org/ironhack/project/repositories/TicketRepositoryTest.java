package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Ticket;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
    }

    @Test
    public void testSaveTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketType.GENERAL_ADMISSION);
        ticket.setStatus(Status.CONFIRMED);
        ticket.setTicketPrice(BigDecimal.valueOf(100.0));

        Ticket savedTicket = ticketRepository.save(ticket);

        assertNotNull(savedTicket.getTicketId());

        System.out.println("Ticket ID after saving: " + savedTicket.getTicketId());
        System.out.println("Ticket Type: " + savedTicket.getTicketType());
        System.out.println("Ticket Price: " + savedTicket.getTicketPrice());
    }

    @Test
    public void testBulkTicketGeneration() {
        int numberOfTicketsToGenerate = 10;
        List<Ticket> tickets = new ArrayList<>();

        for (int i = 0; i < numberOfTicketsToGenerate; i++) {
            Ticket ticket = new Ticket();
            ticket.setTicketType(TicketType.GENERAL_ADMISSION);
            ticket.setStatus(Status.CONFIRMED);
            ticket.setTicketPrice(BigDecimal.valueOf(100.0)); // Set a ticket price for testing

            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);

        List<Ticket> savedTickets = ticketRepository.findAll();
        assertEquals(numberOfTicketsToGenerate, savedTickets.size());

        for (Ticket savedTicket : savedTickets) {
            // Fetch ticketId from savedTicket obtained from repository
            Integer ticketIdFromRepository = savedTicket.getTicketId();

            System.out.println("Ticket to be saved: Ticket(ticketId=" + ticketIdFromRepository +
                    ", ticketPrice=" + savedTicket.getTicketPrice() +
                    ", ticketType=" + savedTicket.getTicketType() +
                    ", status=" + savedTicket.getStatus() +
                    ", concert=" + savedTicket.getConcert() + ")");

            assertNotNull(ticketIdFromRepository);
            assertEquals(TicketType.GENERAL_ADMISSION, savedTicket.getTicketType());
            assertEquals(Status.CONFIRMED, savedTicket.getStatus());
            assertEquals(0, BigDecimal.valueOf(100.0).compareTo(savedTicket.getTicketPrice()));
        }
    }

    @Test
    public void testBulkTicketGenerationWithTicketTypeDistribution() {
        int venueCapacity = 20; // Example venue capacity
        Map<TicketType, Integer> ticketAllowance = new HashMap<>();
        ticketAllowance.put(TicketType.GENERAL_ADMISSION, (int) (venueCapacity * TicketType.GENERAL_ADMISSION.getAllowancePercentage()));
        ticketAllowance.put(TicketType.VIP, (int) (venueCapacity * TicketType.VIP.getAllowancePercentage()));
        ticketAllowance.put(TicketType.EARLY_BIRD, (int) (venueCapacity * TicketType.EARLY_BIRD.getAllowancePercentage()));
        ticketAllowance.put(TicketType.PREMIUM, (int) (venueCapacity * TicketType.PREMIUM.getAllowancePercentage()));
        ticketAllowance.put(TicketType.RESERVED_SEATING, (int) (venueCapacity * TicketType.RESERVED_SEATING.getAllowancePercentage()));

        List<Ticket> tickets = new ArrayList<>();

        for (Map.Entry<TicketType, Integer> entry : ticketAllowance.entrySet()) {
            TicketType ticketType = entry.getKey();
            int ticketsToGenerate = entry.getValue();

            for (int i = 0; i < ticketsToGenerate; i++) {
                Ticket ticket = new Ticket();
                ticket.setTicketType(ticketType);
                ticket.setStatus(Status.CONFIRMED);
                ticket.setTicketPrice(BigDecimal.valueOf(100.0 + i)); // Set ticket price (can be adjusted)

                tickets.add(ticket);
            }
        }

        ticketRepository.saveAll(tickets);

        List<Ticket> savedTickets = ticketRepository.findAll();
        assertEquals(tickets.size(), savedTickets.size());
        System.out.println("Initial Ticket Allowance:");

        savedTickets.forEach(System.out::println);

        for (Ticket savedTicket : savedTickets) {
            Integer ticketIdFromRepository = savedTicket.getTicketId();
        }
    }
}