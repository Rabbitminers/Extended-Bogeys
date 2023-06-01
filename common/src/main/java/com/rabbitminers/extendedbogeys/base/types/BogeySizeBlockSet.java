package com.rabbitminers.extendedbogeys.base.types;

import com.rabbitminers.extendedbogeys.data.ExtendedBogeysBogeySize;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class BogeySizeBlockSet<T extends Block> extends EnumBoundBlockSet<T, ExtendedBogeysBogeySize> {
    public BogeySizeBlockSet(Function<ExtendedBogeysBogeySize, BlockEntry<? extends T>> filler) {
        super(ExtendedBogeysBogeySize.class, filler);
    }
}
