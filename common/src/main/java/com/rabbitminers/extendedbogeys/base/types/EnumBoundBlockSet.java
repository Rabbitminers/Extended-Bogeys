package com.rabbitminers.extendedbogeys.base.types;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Function;

public class EnumBoundBlockSet<T extends Block, E extends Enum<E>>
        implements Iterable<BlockEntry<T>> {

    private final BlockEntry<?>[] values;

    public EnumBoundBlockSet(Class<E> e, Function<E, BlockEntry<? extends T>> filler) {
        values = new BlockEntry<?>[e.getEnumConstants().length];
        for (E constant : e.getEnumConstants()) {
            values[constant.ordinal()] = filler.apply(constant);
        }
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T> get(E cogwheel) {
        return (BlockEntry<T>) values[cogwheel.ordinal()];
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