package com.rabbitminers.extendedbogeys.bogey.sizes;

import net.minecraft.network.chat.TranslatableComponent;

public enum BogeySize {
    SMALL("small", 0.25, true), // Default
    MEDIUM("medium", 0.5, false),
    LARGE("large", 1, true), // Default
    EXTRA_LARGE("extra_large", 1.25, false), // Inbetweeny thing
    HUGE("huge", 2, false); // Flywheel Scale

    private final String name;
    private final double wheelRadius;
    private final boolean isDefault;

    BogeySize(String name, double wheelRadius, boolean isDefault) {
        this.name = name;
        this.wheelRadius = wheelRadius;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
