package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.simibubi.create.content.trains.bogey.BogeySizes;

public class ExtendedBogeysBogeySizes {
    public static final BogeySizes.BogeySize MEDIUM = create("medium", 9f / 16f);
    public static final BogeySizes.BogeySize EXTRA_LARGE = create("extra_large", 38f / 16f);
    public static final BogeySizes.BogeySize HUGE = create("huge", 44f / 16f);

    public static BogeySizes.BogeySize create(String name, float size) {
        return BogeySizes.addSize(ExtendedBogeys.asResource(name), size);
    }

    public static void register() {

    }
}
