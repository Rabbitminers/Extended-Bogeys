package com.rabbitminers.extendedbogeys.bogey.util;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.simibubi.create.AllBlocks;
import net.minecraft.world.level.block.state.BlockState;

public class BogeySizeUtils {
    public static BlockState blockStateFromBogeySize(BogeySize value) {
        return switch (value) {
            case SMALL -> AllBlocks.SMALL_BOGEY.getDefaultState();
            case LARGE -> AllBlocks.LARGE_BOGEY.getDefaultState();
            default -> ExtendedBogeysBlocks.CUSTOM_BOGEYS.get(value).getDefaultState();
        };
    }
}
