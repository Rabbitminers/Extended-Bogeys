package com.rabbitminers.extendedbogeys.mixin_interface;

import net.minecraft.core.Direction;

public interface ICarriageBogeyStyle {
    default Direction getAssemblyDirection() { return Direction.NORTH; }

    default void setAssemblyDirection(Direction assemblyDirection) {};

    default int getStyle() { return 0; }

    default void setStyle(int style) {}

    default boolean isFacingForward() { return true; }

    default void setFacingForward(boolean isFacingForward) {}

}
