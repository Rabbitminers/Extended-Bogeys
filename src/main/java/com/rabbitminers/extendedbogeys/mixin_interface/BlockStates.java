package com.rabbitminers.extendedbogeys.mixin_interface;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockStates {
    public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, 64);
    public static final BooleanProperty IS_FACING_FOWARD = BooleanProperty.create("isfacingforward");
}
