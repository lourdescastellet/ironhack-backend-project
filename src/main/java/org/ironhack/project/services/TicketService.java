package org.ironhack.project.services;

import jakarta.transaction.Transactional;
import org.ironhack.project.models.classes.Concert;
import org.ironhack.project.models.classes.Ticket;
import org.ironhack.project.models.classes.Venue;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;
import org.ironhack.project.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VenueService venueService; // If needed for venue-related operations

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createConcertAndSetVenue(Concert concert, Venue venue) {
        concert.setVenue(venue);
        initializeTicketAllowance(concert); // Initialize ticket allowance when concert is created
    }


    public void initializeTicketAllowance(Concert concert) {
        Venue venue = concert.getVenue();
        int venueCapacity = venue.getVenueCapacity();

        Map<TicketType, Integer> ticketAllowance = new HashMap<>();
        ticketAllowance.put(TicketType.GENERAL_ADMISSION, (int) (venueCapacity * TicketType.GENERAL_ADMISSION.getAllowancePercentage()));
        ticketAllowance.put(TicketType.VIP, (int) (venueCapacity * TicketType.VIP.getAllowancePercentage()));
        ticketAllowance.put(TicketType.EARLY_BIRD, (int) (venueCapacity * TicketType.EARLY_BIRD.getAllowancePercentage()));
        ticketAllowance.put(TicketType.PREMIUM, (int) (venueCapacity * TicketType.PREMIUM.getAllowancePercentage()));
        ticketAllowance.put(TicketType.RESERVED_SEATING, (int) (venueCapacity * TicketType.RESERVED_SEATING.getAllowancePercentage()));

        concert.setTicketAllowance(ticketAllowance);
    }

    public void calculateTicketPrice(Ticket ticket, BigDecimal originalPrice) {
        TicketType ticketType = ticket.getTicketType();
        if (ticketType == null) {
            throw new IllegalArgumentException("Ticket type cannot be null");
        }

        BigDecimal priceMultiplier = BigDecimal.valueOf(ticketType.getPriceMultiplier());
        BigDecimal ticketPrice = originalPrice.multiply(priceMultiplier);
        ticket.setTicketPrice(ticketPrice.setScale(2, RoundingMode.HALF_UP));
    }

    public void generateTicketsForConcert(Concert concert, BigDecimal originalPrice) {
        // Initialize ticket allowance based on concert's venue
        initializeTicketAllowance(concert);

        // Generate tickets for the concert
        List<Ticket> tickets = getTickets(concert);

        // Log the generated tickets
        System.out.println("Generated tickets:");
        tickets.forEach(System.out::println);

        // Save generated tickets
        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        // Log the saved tickets
        System.out.println("Saved tickets:");
        savedTickets.forEach(System.out::println);

        // For each ticket, calculate the ticket price based on original price and ticket type
        savedTickets.forEach(ticket -> calculateTicketPrice(ticket, originalPrice));

        // Add generated tickets to concert object
        concert.getTickets().addAll(savedTickets);
    }

    private static List<Ticket> getTickets(Concert concert) {
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

}
