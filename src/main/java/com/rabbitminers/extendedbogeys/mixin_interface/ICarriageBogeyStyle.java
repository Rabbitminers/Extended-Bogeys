package com.rabbitminers.extendedbogeys.mixin_interface;

import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

public interface ICarriageBogeyStyle {
    default Direction getAssemblyDirection() { return Direction.NORTH; }

    default void setAssemblyDirection(Direction assemblyDirection) {};

    default int getStyle() { return 0; }

    default void setStyle(int style) {}

    default boolean isFacingForward() { return true; }
    default void setFacingForward(boolean isFacingForward) {}

    /**
    * @return Dye colour applied to the bogey, return null if it is default
     **/
    default DyeColor getPaintColour() { return null; } // yes its colour fuck off

    default void setPaintColour(DyeColor colour) {}
}
