package com.rabbitminers.extendedbogeys.mixin_interface;

public interface ICarriageContraptionEntity {
    default double getDistanceTo() { return 0; }
    default void setDistanceTo() {}
}
