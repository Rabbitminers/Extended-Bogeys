package com.rabbitminers.extendedbogeys.mixin_interface;

import net.minecraft.nbt.CompoundTag;

public interface IStyledStandardBogeyTileEntity {
    default boolean getIsFacingForwards(CompoundTag tileData) { return true; }

    default void setIsFacingForwards(CompoundTag tileData, boolean isFacingForwards) {}

    default int getBogeyStyle(CompoundTag tileData) { return 0; }

    default void setBogeyStyle(CompoundTag tileData, int bogeyStyle) {}
}
