package com.rabbitminers.extendedbogeys.mixin_interface;

public interface IStyledStandardBogeyTileEntity {
    default boolean getIsFacingForwards() { return true; }

    default void setIsFacingForwards(boolean isFacingForwards) {}

    default int getBogeyStyle() { return 0; }

    default void setBogeyStyle(int bogeyStyle) {}
}
