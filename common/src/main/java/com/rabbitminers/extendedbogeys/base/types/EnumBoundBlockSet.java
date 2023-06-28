package com.rabbitminers.extendedbogeys.base.types;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class EnumBoundBlockSet<T extends Block, E extends Enum<E>>
        implements Iterable<BlockEntry<T>> {
    private final BlockEntry<?>[] values;
    private final Class<E> e;

    public EnumBoundBlockSet(Class<E> e, Function<E, BlockEntry<? extends T>> filler) {
        this.e = e;
        values = new BlockEntry<?>[e.getEnumConstants().length];

        for (E constant : e.getEnumConstants()) {
            values[constant.ordinal()] = filler.apply(constant);
        }
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T> get(E block) {
        return (BlockEntry<T>) values[block.ordinal()];
    }

    @Nullable
    public E enumValueOfBlock(T block) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].get() == block)
                return e.getEnumConstants()[i];
        }
        return null;
    }

    public boolean contains(Block block) {
        for (BlockEntry<?> entry : values) {
            if (entry.is(block)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Item item) {
        return item instanceof BlockItem bi && this.contains(bi.getBlock());
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