package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogeys.renderers.SingleAxleBogeyRenderer.SmallSingleAxleBogeyRenderer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.foundation.utility.Components;

public class ExtendedBogeysBogeyStyles {
    public final BogeyStyle SINGLE_AXLE = create("single_axle")
            .displayName(Components.translatable("extendedbogeys.bogey.style.standard"))
            .size(BogeySizes.SMALL, () -> SmallSingleAxleBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
            .build();

    public static AllBogeyStyles.BogeyStyleBuilder create(String name, String cycleGroup) {
        return AllBogeyStyles.create(ExtendedBogeys.asResource(name), ExtendedBogeys.asResource(cycleGroup));
    }

    public static AllBogeyStyles.BogeyStyleBuilder create(String name) {
        return AllBogeyStyles.create(ExtendedBogeys.asResource(name), Create.asResource(AllBogeyStyles.STANDARD_CYCLE_GROUP));
    }

    public static void register() {

    }
}
