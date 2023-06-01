package com.rabbitminers.extendedbogeys.data;


import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeySizes;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum ExtendedBogeysBogeySize {
    SMALL(BogeySizes.SMALL, true),
    MEDIUM(ExtendedBogeysBogeySizes.MEDIUM),
    LARGE(BogeySizes.LARGE, true),
    EXTRA_LARGE(ExtendedBogeysBogeySizes.EXTRA_LARGE),
    HUGE(ExtendedBogeysBogeySizes.HUGE)
    ;

    public final BogeySizes.BogeySize size;
    public final boolean inBuilt;

    ExtendedBogeysBogeySize(BogeySizes.BogeySize size, boolean inBuilt) {
        this.size = size;
        this.inBuilt = inBuilt;
    }

    ExtendedBogeysBogeySize(BogeySizes.BogeySize size) {
        this.size = size;
        this.inBuilt = false;
    }

    public static ExtendedBogeysBogeySize[] iterateCustom() {
        return Arrays.stream(ExtendedBogeysBogeySize.values())
                .filter(value -> !value.inBuilt)
                .toArray(ExtendedBogeysBogeySize[]::new);
    }

    public String id() {
        return this.size.location().getPath();
    }

    @Nullable
    public static ExtendedBogeysBogeySize of(BogeySizes.BogeySize size) {
        return Arrays.stream(values())
                .filter(value -> value.size == size)
                .findFirst()
                .orElse(null);
    }
}
