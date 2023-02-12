package com.rabbitminers.extendedbogeys.bogey.sizes;

import com.simibubi.create.content.logistics.trains.track.TrackBlock;

public enum BogeySize {
    SMALL("small", "extendedbogeys.bogeys.sizes.small", 0.25, false, true), // Default
    MEDIUM("medium", "extendedbogeys.bogeys.sizes.medium", 0.5, false, false),
    LARGE("large", "extendedbogeys.bogeys.sizes.large", 1, true,true), // Default
    EXTRA_LARGE("extra_large", "extendedbogeys.bogeys.sizes.extra_large", 1.25, true,false), // Inbetweeny thing
    HUGE("huge", "extendedbogeys.bogeys.sizes.huge", 2, true, false); // Flywheel Scale

    private final String name;
    private final String translationKey;
    private final double wheelRadius;
    private final boolean isDriver;
    private final boolean isDefault;

    BogeySize(String name, String displayedName, double wheelRadius, boolean isDriver, boolean isDefault) {
        this.name = name;
        this.translationKey = displayedName;
        this.wheelRadius = wheelRadius;
        this.isDriver = isDriver;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
