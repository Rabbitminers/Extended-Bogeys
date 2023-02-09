package com.rabbitminers.extendedbogeys.bogey.util;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BogeyBlockList<T extends Block> implements Iterable<BlockEntry<T>> {
    private static final int SIZE_AMOUNT = BogeySize.values().length;

    private final BlockEntry<?>[] values = new BlockEntry<?>[SIZE_AMOUNT];

    public BogeyBlockList(Function<BogeySize, BlockEntry<? extends T>> filler) {
        for (BogeySize size : BogeySize.values()) {
            values[size.ordinal()] = filler.apply(size);
        }
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T> get(BogeySize size) {
        return (BlockEntry<T>) values[size.ordinal()];
    }

    public boolean contains(Block block) {
        for (BlockEntry<?> entry : values) {
            if (entry.is(block)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T>[] toArray() {
        return (BlockEntry<T>[]) Arrays.copyOf(values, values.length);
    }

    @Override
    public Iterator<BlockEntry<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @SuppressWarnings("unchecked")
            @Override
            public BlockEntry<T> next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return (BlockEntry<T>) values[index++];
            }
        };
    }
}
