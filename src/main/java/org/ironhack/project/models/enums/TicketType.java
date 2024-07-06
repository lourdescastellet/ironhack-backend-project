package org.ironhack.project.models.enums;

import lombok.Getter;

@Getter
public enum TicketType {

    VIP(2, 0.05),  // 200% of the base price, 5% of the capacity
    GENERAL_ADMISSION(1.0, 0.40),  // Base price, 40% of the capacity
    EARLY_BIRD(0.9, 0.05),  // 90% of the base price, 5% of the capacity
    PREMIUM(1.5, 0.20),  // 125% of the base price, 20% of the capacity
    RESERVED_SEATING(1.25, 0.30);  // 110% of the base price, 30% of the capacity

    private final double priceMultiplier;
    private final double allowancePercentage;

    TicketType(double priceMultiplier, double allowancePercentage) {
        this.priceMultiplier = priceMultiplier;
        this.allowancePercentage = allowancePercentage;
    }

}
