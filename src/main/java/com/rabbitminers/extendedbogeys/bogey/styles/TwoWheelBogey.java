package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<GuiGameElement.GuiRenderBuilder> renderSmallInGuiOverlay() {
        return IBogeyStyle.super.renderSmallInGuiOverlay();
    }

    @Override
    public List<GuiGameElement.GuiRenderBuilder> renderInGuiOverlay(boolean isLarge) {
        return IBogeyStyle.super.renderInGuiOverlay(isLarge);
    }

    @Override
    public List<GuiGameElement.GuiRenderBuilder> renderLargeInGuiOverlay() {
        GuiGameElement.GuiRenderBuilder
                frame = GuiGameElement.of(AllBlockPartials.BOGEY_FRAME),
                front_wheel = GuiGameElement.of(AllBlockPartials.LARGE_BOGEY_WHEELS).atLocal(-1, 0, 0),
                back_wheel = GuiGameElement.of(AllBlockPartials.LARGE_BOGEY_WHEELS).atLocal(1, 0, 0);
        return new ArrayList<>(Arrays.asList(frame, front_wheel, back_wheel));
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager) {
        IBogeyStyle.super.registerLargeBogeyModelData(materialManager);
    }

    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager) {
        IBogeyStyle.super.registerSmallBogeyModelData(materialManager);
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
