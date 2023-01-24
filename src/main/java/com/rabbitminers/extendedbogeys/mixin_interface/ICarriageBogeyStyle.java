package com.rabbitminers.extendedbogeys.mixin_interface;

public interface ICarriageBogeyStyle {
    default int getStyle() { return 0; }

    default void setStyle(int style) {}

    default boolean isFacingForward() { return true; }

    default void setFacingForward(boolean isFacingForward) {}

}
