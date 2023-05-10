package com.rabbitminers.extendedbogeys.mixin_interface;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public interface IStyledStandardBogeyTileEntity {
    default boolean getIsFacingForwards(CompoundTag tileData) { return true; }

    default void setIsFacingForwards(CompoundTag tileData, boolean isFacingForwards) {}

    default int getBogeyStyle(CompoundTag tileData) { return 0; }

    default void setBogeyStyle(CompoundTag tileData, int bogeyStyle) {}

    // TODO Implement these into child classes
    default Direction getAssemblyDirection(CompoundTag tileData) {return Direction.NORTH; }

    default void setAssemblyDirection(CompoundTag tileData, Direction assemblyDirection) {}

    default DyeColor getPaintColour(CompoundTag tileData) { return DyeColor.GRAY; } // yes its colour fuck off

    default void setPaintColour(CompoundTag tileData, DyeColor colour) {}
}
