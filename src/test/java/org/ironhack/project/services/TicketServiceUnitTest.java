package org.ironhack.project.services;

import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Ticket;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;
import org.ironhack.project.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceUnitTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Concert concert;
    private Venue venue;

    @BeforeEach
    void setUp() {
        venue = new Venue();
        venue.setVenueCapacity(20);

        concert = new Concert();
        concert.setConcertName("Rock Fest");

        ticketService.createConcertAndSetVenue(concert, venue);

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testSetupTicketTypesAndGenerateTickets() {
        BigDecimal originalPrice = BigDecimal.valueOf(100); // Adjust as needed

        // Mock behavior of ticketRepository.saveAll to capture the tickets argument
        ArgumentCaptor<List<Ticket>> captor = ArgumentCaptor.forClass(List.class);

        // Mock saveAll method to capture the tickets argument and return them
        Mockito.when(ticketRepository.saveAll(captor.capture())).thenAnswer(invocation -> {
        List<Ticket> tickets = captor.getValue();
        for (Ticket ticket : tickets) {
            ticket.setTicketId(1); // Simulate saving tickets (optional)
        }
        return tickets;
        });

        ticketService.generateTicketsForConcert(concert, originalPrice);

        // Retrieve captured tickets
        List<Ticket> savedTickets = captor.getValue();

        // Assert that tickets were saved correctly
        assertEquals(20, savedTickets.size());

        for (Ticket savedTicket : savedTickets) {
            assertNotNull(savedTicket.getTicketId());
            assertNotNull(savedTicket.getTicketType());
            assertNotNull(savedTicket.getStatus());

            System.out.println("Ticket ID: " + savedTicket.getTicketId());
            System.out.println("Ticket Type: " + savedTicket.getTicketType());
            System.out.println("Ticket Status: " + savedTicket.getStatus());
            System.out.println("--------------------");
        }
    }

    @Test
    public void testAssignPricesToTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        BigDecimal originalPrice = BigDecimal.valueOf(75);

        for (Ticket ticket : tickets) {
            ticketService.calculateTicketPrice(ticket, originalPrice);
        }

        ticketRepository.saveAll(tickets);

        List<Ticket> updatedTickets = ticketRepository.findAll();

        for (Ticket updatedTicket : updatedTickets) {
            assertNotNull(updatedTicket.getTicketPrice());
            System.out.println("Ticket ID: " + updatedTicket.getTicketId());
            System.out.println("Ticket Type: " + updatedTicket.getTicketType());
            System.out.println("Ticket Price: " + updatedTicket.getTicketPrice());
            System.out.println("--------------------");
        }
    }

    @Test
    public void testAssignTicketTypeAndCalculatePrice() {
        // Initialize ticket allowance based on concert's venue
        ticketService.initializeTicketAllowance(concert);

        List<Ticket> tickets = getTickets();

        // Save generated tickets
        ticketRepository.saveAll(tickets);

        // For each ticket, calculate the ticket price based on original price
        BigDecimal originalPrice = BigDecimal.valueOf(100); // Adjust as needed
        tickets.forEach(ticket -> {
            ticketService.calculateTicketPrice(ticket, originalPrice);
        });

        // Validate each ticket has a non-null ticket type and price assigned
        for (Ticket ticket : tickets) {
            assertNotNull(ticket.getTicketType());
            assertNotNull(ticket.getTicketPrice());

            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Ticket Type: " + ticket.getTicketType());
            System.out.println("Ticket Price: " + ticket.getTicketPrice());
            System.out.println("--------------------");
        }
    }

    private List<Ticket> getTickets() {
        Map<TicketType, Integer> ticketAllowance = concert.getTicketAllowance();

        // Loop through each ticket type and create tickets
        List<Ticket> tickets = new ArrayList<>();
        ticketAllowance.forEach((ticketType, allowance) -> {
            for (int i = 0; i < allowance; i++) {
                Ticket ticket = new Ticket();
                ticket.setTicketType(ticketType);
                ticket.setStatus(Status.CONFIRMED); // Assuming status is set in actual logic
                tickets.add(ticket);
            }
        });
        return tickets;
    }

    @Test
    public void testGenerateTicketsForConcert() {
        BigDecimal originalPrice = BigDecimal.valueOf(100); // Adjust as needed

        // Use Mockito.anyList() to match any List type for saveAll method
        Mockito.when(ticketRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> {
            List<Ticket> tickets = invocation.getArgument(0);
            for (Ticket ticket : tickets) {
                ticket.setTicketId(1); // Simulate saving tickets (optional)
            }
            return tickets;
        });

        // Initialize ticket allowance based on concert's venue
        ticketService.initializeTicketAllowance(concert);

        // Generate tickets for the concert
        List<Ticket> tickets = getTickets();

        // Save generated tickets
        ticketRepository.saveAll(tickets);

        // For each ticket, calculate the ticket price based on original price
        tickets.forEach(ticket -> ticketService.calculateTicketPrice(ticket, originalPrice));

        // Validate each ticket has a non-null ticket type and price assigned
        for (Ticket ticket : tickets) {
            assertNotNull(ticket.getTicketType());
            assertNotNull(ticket.getTicketPrice());

            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Ticket Type: " + ticket.getTicketType());
            System.out.println("Ticket Price: " + ticket.getTicketPrice());
            System.out.println("Concert: " + concert.getConcertName());
            System.out.println("--------------------");
        }

        // Further assertions if needed
        assertEquals(20, tickets.size()); // Adjust based on your test scenario
    }

}
