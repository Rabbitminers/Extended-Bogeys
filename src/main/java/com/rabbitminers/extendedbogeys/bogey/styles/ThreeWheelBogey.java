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

public class ThreeWheelBogey implements IBogeyStyle {
    // TODO: Use language keys
    private final int STYLE_ID = 2;
    private final String STYLE_NAME = "Three Wheel Bogey";
    private ModelData threeWheelBogeyRod;
    private ModelData frame;
    private ModelData[] wheels;

    @Override
    public List<ModelData> getAllCustomModelComponents() {
        List<ModelData> modelData = new ArrayList<>();
        modelData.add(frame);
        modelData.add(threeWheelBogeyRod);
        modelData.addAll(Arrays.asList(wheels));
        return modelData;
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BOGEY_FRAME, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);
        CachedBufferer.partial(BogeyPartials.THREE_WHEEL_BOGEY_ROD, air)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .light(light)
                .renderInto(ms, vb);
        for (int side = -1; side < 2; side++) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side * 1.75)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BOGEY_FRAME, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side = -1; side < 2; side++) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side * 1.25)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }
        IBogeyStyle.super.renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    @Override
    public void renderLargeInContraption(float wheelAngle, PoseStack ms) {
        for (int side = -1; side < 2; side++) {
            wheels[side + 1].setTransform(ms)
                    .translate(0, 12 / 16f, side * 1.75)
                    .rotateX(wheelAngle);
        }

        threeWheelBogeyRod.setTransform(ms)
                .translate(0, 0, 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle);

        frame.setTransform(ms);

        IBogeyStyle.super.renderLargeInContraption(wheelAngle, ms);
    }

    @Override
    public void renderSmallInContraption(float wheelAngle, PoseStack ms) {
        for (int side = -1; side < 2; side++) {
            wheels[side + 1].setTransform(ms)
                    .translate(0, 12 / 16f, side * 1.25)
                    .rotateX(wheelAngle);
        }

        frame.setTransform(ms);

        IBogeyStyle.super.renderSmallInContraption(wheelAngle, ms);
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.BOGEY_FRAME)
                .createInstance();

        threeWheelBogeyRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.THREE_WHEEL_BOGEY_ROD)
                .createInstance();

        wheels = new ModelData[3];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.LARGE_BOGEY_WHEELS)
                .createInstances(wheels);
    }

    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.BOGEY_FRAME)
                .createInstance();

        threeWheelBogeyRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.THREE_WHEEL_BOGEY_ROD)
                .createInstance();

        wheels = new ModelData[3];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.SMALL_BOGEY_WHEELS)
                .createInstances(wheels);
    }

    @Override
    public float getMinimumTurnRadius() {
        return IBogeyStyle.super.getMinimumTurnRadius();
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
