package com.rabbitminers.extendedbogeys.bogey.sizes;

import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import net.minecraft.core.Direction;

public enum BogeySize {
    SMALL(0), // Default
    MEDIUM(0),
    LARGE(0), // Default
    EXTRA_LARGE(0), // Inbetweeny thing
    HUGE(0); // Flywheel Scale

    private final double wheelRadius;
    BogeySize(double wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }
}
