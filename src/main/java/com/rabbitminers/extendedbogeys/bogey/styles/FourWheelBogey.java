package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.level.block.state.BlockState;

public class FourWheelBogey implements IBogeyStyle {
    private static final String STYLE_NAME = "Four Wheel Bogey";
    private ModelData frame;
    private ModelData[] wheels;
    private ModelData drivePin;
    private ModelData driveRod;
    private ModelData connectingRod;

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        float zOffset = Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f) * 0.2f);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_PIN, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translate(0, 0, isFacingForward ? -zOffset : zOffset)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 1, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        IBogeyStyle.super.renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }
}
