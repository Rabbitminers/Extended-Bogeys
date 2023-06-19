package com.rabbitminers.extendedbogeys.base.types;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class PaintedBlockList<T extends Block> extends EnumBoundBlockSet<T, DyeColor> {

    public PaintedBlockList(Function<DyeColor, BlockEntry<? extends T>> filler) {
        super(DyeColor.class, filler);
    }
}
