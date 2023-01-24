package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleAxisBogey implements IBogeyStyle {
    private static final String STYLE_NAME = "Single Axis";

    private ModelData frame;
    private ModelData pin;
    private ModelData wheels;

    @Override
    public List<ModelData> getAllCustomModelComponents() {
        List<ModelData> modelData = new ArrayList<>();
        modelData.add(frame);
        modelData.add(pin);
        modelData.add(wheels);
        return modelData;
    }
    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME)
                .createInstance();

        pin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_PIN)
                .createInstance();

        wheels = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.SMALL_BOGEY_WHEELS)
                .createInstance();
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME)
                .createInstance();

        pin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_PIN)
                .createInstance();

        wheels = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.SMALL_BOGEY_WHEELS)
                .createInstance();
    }
    @Override
    public void renderSmallInContraption(float wheelAngle, PoseStack ms) {
        wheels.setTransform(ms)
                .translate(0, 12 / 16f, -1)
                .rotateX(wheelAngle);

        frame.setTransform(ms).translate(0, 0.1, 0);

        pin.setTransform(ms);

        IBogeyStyle.super.renderSmallInContraption(wheelAngle, ms);
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        IBogeyStyle.super.renderLargeInWorld(wheelAngle, ms, light, vb, air);
    }
    @Override
    public void renderSmallInWorld(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME, air)
                .translate(0, 0.1, 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_PIN, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        ms.pushPose();

        CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                .translate(0, 12 / 16f, -1)
                .rotateX(wheelAngle)
                .light(light)
                .renderInto(ms, vb);

        ms.popPose();
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }

    @Override
    public String getStyleName() {
        return STYLE_NAME;
    }
}
