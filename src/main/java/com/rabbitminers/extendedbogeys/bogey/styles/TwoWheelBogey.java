package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.level.block.state.BlockState;

public class TwoWheelBogey implements IBogeyStyle {
    private final int STYLE_ID = 1;
    // TODO: Implement Translation Keys
    private final String STYLE_NAME = "Two Wheel";

    @Override
    public void renderLargeInWorld(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BOGEY_FRAME, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);
        CachedBufferer.partial(BogeyPartials.TWO_WHEEL_BOGEY_ROD, air)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .light(light)
                .renderInto(ms, vb);
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderSmallInWorld(wheelAngle, ms, light, vb, air);
    }

    @Override
    public void renderSmallInWorld(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BOGEY_FRAME, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderSmallInWorld(wheelAngle, ms, light, vb, air);
    }

    @Override
    public void renderLargeInContraption(MaterialManager materialManager) {
        IBogeyStyle.super.renderLargeInContraption(materialManager);
    }

    @Override
    public void renderSmallInContraption(MaterialManager materialManager) {
        IBogeyStyle.super.renderSmallInContraption(materialManager);
    }

    @Override
    public int getStyleId() {
        return STYLE_ID;
    }

    @Override
    public String getStyleName() {
        return STYLE_NAME;
    }
}
