package com.rabbitminers.extendedbogeys.mixin.client;

import com.rabbitminers.extendedbogeys.bogeys.styles.PaintableStandardBogeyRenderer;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(AllBogeyStyles.class)
public class MixinAllBogeyStyles {
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/AllBogeyStyles$BogeyStyleBuilder;size(Lcom/simibubi/create/content/trains/bogey/BogeySizes$BogeySize;Ljava/util/function/Supplier;Lcom/tterrag/registrate/util/entry/BlockEntry;)Lcom/simibubi/create/AllBogeyStyles$BogeyStyleBuilder;",
                    ordinal = 0
            ),
            index = 1
    )
    private static Supplier<Supplier<? extends BogeyRenderer>> setSmallRenderer(Supplier<Supplier<? extends BogeyRenderer>> renderer) {
        return () -> PaintableStandardBogeyRenderer.SmallStandardBogeyRenderer::new;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/AllBogeyStyles$BogeyStyleBuilder;size(Lcom/simibubi/create/content/trains/bogey/BogeySizes$BogeySize;Ljava/util/function/Supplier;Lcom/tterrag/registrate/util/entry/BlockEntry;)Lcom/simibubi/create/AllBogeyStyles$BogeyStyleBuilder;",
                    ordinal = 1
            ),
            index = 1
    )
    private static Supplier<Supplier<? extends BogeyRenderer>> setLargeRenderer(Supplier<Supplier<? extends BogeyRenderer>> renderer) {
        return () -> PaintableStandardBogeyRenderer.LargeStandardBogeyRenderer::new;
    }
}
