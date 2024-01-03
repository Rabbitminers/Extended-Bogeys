package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogeys.styles.DoubleAxisTrailingRenderer.MediumDoubleAxisTrailingRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.DoubleAxleBogeyRenderer.LargeDoubleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.DoubleAxleBogeyRenderer.SmallDoubleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.QuadrupleAxisBogeyRenderer.MediumQuadrupleAxisBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.QuadrupleAxisTenderRenderer.MediumQuadrupleAxisTenderRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.QuintupleAxisBogeyRenderer.MediumQuintupleAxisBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.QuintupleAxisTenderRenderer.MediumQuintupleAxisTenderRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.SingleAxisBogeyRenderer.MediumSingleAxisBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.SingleAxisBogeyRenderer.SmallSingleAxisBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.SingleAxisTrailingRenderer.MediumSingleAxisTrailingRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.TripleAxisTenderRenderer.MediumTripleAxisTenderRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.TripleAxisTrailingRenderer.MediumTripleAxisTrailingRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.TripleAxleBogeyRenderer.LargeTripleAxleBogeyRenderer;
import com.rabbitminers.extendedbogeys.bogeys.styles.TripleAxleBogeyRenderer.MediumTripleAxisBogeyRenderer;
import com.rabbitminers.extendedbogeys.data.ExtendedBogeysBogeySize;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.foundation.utility.Components;

import static com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeySizes.MEDIUM;
import static com.simibubi.create.content.trains.bogey.BogeySizes.LARGE;
import static com.simibubi.create.content.trains.bogey.BogeySizes.SMALL;

public class ExtendedBogeysBogeyStyles {
    public static final BogeyStyle SINGLE_AXLE_BOGEY = create("single_axle")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.single_axle"))
            .size(SMALL, () -> SmallSingleAxisBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
            .size(MEDIUM, () -> MediumSingleAxisBogeyRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();
    
    public static final BogeyStyle SINGLE_AXLE_TRAIlER = create("single_axle_trailer")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.single_axle_trailer"))
            .size(MEDIUM, () -> MediumSingleAxisTrailingRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle DOUBLE_AXLE_BOGEY = create("double_axle")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.double_axle"))
            .size(SMALL, () -> SmallDoubleAxleBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
            .size(LARGE, () -> LargeDoubleAxleBogeyRenderer::new, AllBlocks.LARGE_BOGEY)
            .build();

    public static final BogeyStyle DOUBLE_AXLE_TRAILER = create("double_axle_trailer")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.double_axle_trailer"))
            .size(MEDIUM, () -> MediumDoubleAxisTrailingRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle TRIPLE_AXLE_BOGEY = create("triple_axle")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.triple_axle"))
            .size(MEDIUM, () -> MediumTripleAxisBogeyRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .size(LARGE, () -> LargeTripleAxleBogeyRenderer::new, AllBlocks.LARGE_BOGEY)
            .build();

    public static final BogeyStyle TRIPLE_AXLE_TENDER = create("triple_axle_tender")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.triple_axle_tender"))
            .size(MEDIUM, () -> MediumTripleAxisTenderRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle TRIPLE_AXLE_TRAILER = create("triple_axle_trailer")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.triple_axle_trailer"))
            .size(MEDIUM, () -> MediumTripleAxisTrailingRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle QUADRUPLE_AXLE_BOGEY = create("quadruple_axle")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.quadruple_axle"))
            .size(MEDIUM, () -> MediumQuadrupleAxisBogeyRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle QUADRUPLE_AXLE_TENDER = create("quadruple_axle_tender")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.quadruple_axle_tender"))
            .size(MEDIUM, () -> MediumQuadrupleAxisTenderRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle QUINTUPLE_AXLE_BOGEY = create("quintuple_axle")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.quintuple_axle"))
            .size(MEDIUM, () -> MediumQuintupleAxisBogeyRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
            .build();

    public static final BogeyStyle QUINTUPLE_AXLE_TENDER = create("quintuple_axle_tender")
            .displayName(Components.translatable("extendedbogeys.bogeys.styles.quintuple_axle_tender"))
            .size(MEDIUM, () -> MediumQuintupleAxisTenderRenderer::new, ExtendedBogeysBlocks.STANDARD_BOGEYS.get(ExtendedBogeysBogeySize.MEDIUM))
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
