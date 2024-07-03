package org.ironhack.project.models.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.ironhack.project.models.enums.Status;
import org.ironhack.project.models.enums.TicketType;
import java.math.BigDecimal;
import java.math.RoundingMode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends Event{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    private BigDecimal ticketPrice;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Concert concert;

    public void calculateTicketPrice(BigDecimal basePrice) {
        BigDecimal priceMultiplier = BigDecimal.valueOf(ticketType.getPriceMultiplier());
        ticketPrice = basePrice.multiply(priceMultiplier);

    }

    public BigDecimal calculateArtistProfit() {
        BigDecimal artistProfit = BigDecimal.ZERO;
        if (concert != null && concert.getArtist() != null) {
            BigDecimal priceMultiplier = BigDecimal.valueOf(ticketType.getPriceMultiplier());
            BigDecimal basePrice = ticketPrice.divide(priceMultiplier, 2, RoundingMode.HALF_UP);
            BigDecimal ticketRevenue = ticketPrice.subtract(basePrice);

            BigDecimal artistPercentage = BigDecimal.valueOf(0.60); // Artist gets 60%
            artistProfit = ticketRevenue.multiply(artistPercentage);
        }
        return artistProfit;
    }

    public BigDecimal calculateVenueProfit() {
        BigDecimal venueProfit = BigDecimal.ZERO;
        if (concert != null && concert.getVenue() != null) {
            BigDecimal priceMultiplier = BigDecimal.valueOf(ticketType.getPriceMultiplier());
            BigDecimal basePrice = ticketPrice.divide(priceMultiplier, 2, RoundingMode.HALF_UP);
            BigDecimal ticketRevenue = ticketPrice.subtract(basePrice);

            BigDecimal venuePercentage = BigDecimal.valueOf(0.30); // Venue gets 30%
            venueProfit = ticketRevenue.multiply(venuePercentage);
        }
        return venueProfit;
    }

    public BigDecimal calculateAdminProfit() {
        BigDecimal adminProfit = BigDecimal.ZERO;
        if (concert != null && concert.getAdmin() != null) {
            BigDecimal priceMultiplier = BigDecimal.valueOf(ticketType.getPriceMultiplier());
            BigDecimal basePrice = ticketPrice.divide(priceMultiplier, 2, RoundingMode.HALF_UP);
            BigDecimal ticketRevenue = ticketPrice.subtract(basePrice);

            BigDecimal adminPercentage = BigDecimal.valueOf(0.10); // Admin gets 10%
            adminProfit = ticketRevenue.multiply(adminPercentage);
        }
        return adminProfit;
    }

}
