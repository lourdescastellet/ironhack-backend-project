package org.ironhack.project.models.enums;

public enum TicketType {
    VIP(2.0),  // 200% of the base price
    GENERAL_ADMISSION(1.0),  // Base price
    EARLY_BIRD(0.9),  // 90% of the base price
    PREMIUM(1.25),  // 125% of the base price
    RESERVED_SEATING(1.1);  // 110% of the base price

    private final double priceMultiplier;

    TicketType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

}
