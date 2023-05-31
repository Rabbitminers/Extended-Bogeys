package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogeys.styles.DoubleAxleBogeyRenderer.LargeDoubleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.DoubleAxleBogeyRenderer.SmallDoubleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.SingleAxleBogeyRenderer.SmallSingleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.TripleAxleBogeyRenderer.LargeTripleAxleBogeyRenderer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.foundation.utility.Components;

public class ExtendedBogeysBogeyStyles {
    public static final BogeyStyle SINGLE_AXLE = create("single_axle")
            .displayName(Components.translatable("extendedbogeys.bogey.style.single_axle"))
            .size(BogeySizes.SMALL, () -> SmallSingleAxleBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
            .build();

    public static final BogeyStyle DOUBLE_AXLE = create("double_axle")
            .displayName(Components.translatable("extendedbogeys.bogey.style.double_axle"))
            .size(BogeySizes.SMALL, () -> SmallDoubleAxleBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
            .size(BogeySizes.LARGE, () -> LargeDoubleAxleBogeyRenderer::new, AllBlocks.LARGE_BOGEY)
            .build();

    public static final BogeyStyle TRIPLE_AXLE = create("triple_axle")
            .displayName(Components.translatable("extendedbogeys.bogey.style.triple_axle"))
            .size(BogeySizes.LARGE, () -> LargeTripleAxleBogeyRenderer::new, AllBlocks.LARGE_BOGEY)
            .build();

    public static AllBogeyStyles.BogeyStyleBuilder create(String name, String cycleGroup) {
        return AllBogeyStyles.create(ExtendedBogeys.asResource(name), ExtendedBogeys.asResource(cycleGroup));
    }

    public static AllBogeyStyles.BogeyStyleBuilder create(String name) {
        return AllBogeyStyles.create(ExtendedBogeys.asResource(name), Create.asResource(AllBogeyStyles.STANDARD_CYCLE_GROUP));
    }

    public static void register() {
        ExtendedBogeys.LOGGER.info("Registered bogey styles for " + ExtendedBogeys.MOD_NAME);
    }
}
