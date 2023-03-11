package com.rabbitminers.extendedbogeys.bogey.styles.content;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;
import com.rabbitminers.extendedbogeys.bogey.util.RotationUtils;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourWheelBogey implements IBogeyStyle {
    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.fourwheel").getString();
    }

    private ModelData frame;
    private ModelData[] wheels;
    private ModelData drivePin;
    private ModelData driveRod;
    private ModelData connectingRod;
    private ModelData[] shafts;

    @Override
    public List<ModelData> getAllCustomModelComponents() {
        List<ModelData> modelData = new ArrayList<>();
        modelData.add(frame);
        modelData.add(drivePin);
        modelData.add(driveRod);
        modelData.addAll(List.of(wheels));
        modelData.add(connectingRod);
        return modelData;
    }

    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SMALL_FOUR_WHEEL_FRAME)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SMALL_FOUR_WHEEL_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_ROD)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.SMALL_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD)
                .createInstance();
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_DRIVE_ROD)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.LARGE_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD)
                .createInstance();

        shafts = new ModelData[2];

        materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(AllBlocks.SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X))
                .createInstances(shafts);
    }

    @Override
    public void renderLargeInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms, Direction assemblyDirection) {
        boolean isDirectionPosotive = RotationUtils.isDirectionPosotive(assemblyDirection);
        isFacingForward = isDirectionPosotive == isFacingForward;
        wheelAngle = isFacingForward ? wheelAngle : -wheelAngle;

        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));
        double offset = 3d / 16d;

        for (int side : Iterate.positiveAndNegative) {
            wheels[(side+1) / 2].setTransform(ms)
                    .translate(0, 1, side)
                    .rotateX(isFacingForward ? wheelAngle : -wheelAngle);
        }

        connectingRod.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle);

        ms.translate(0, offset, isFacingForward ? offset : -offset);

        drivePin.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)));

        driveRod.setTransform(ms)
                .translateZ(isFacingForward ? -0.6 : 0.6)
                .translateY(0.85)
                .rotateY(isFacingForward ? 0 : 180)
                .rotateX(offSetScaleFactor*20)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)));

        frame.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180);

        IBogeyStyle.super.renderLargeInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
    }

    @Override
    public void renderSmallInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms, Direction assemblyDirection) {
        boolean isDirectionPosotive = RotationUtils.isDirectionPosotive(assemblyDirection);
        isFacingForward = isDirectionPosotive == isFacingForward;
        wheelAngle = isFacingForward ? wheelAngle : -wheelAngle;

        for (int side : Iterate.positiveAndNegative) {
            wheels[(side+1) / 2].setTransform(ms)
                    .translate(0, 12 / 16f, side + (isFacingForward ? -2 : 2))
                    .rotateX(isFacingForward ? wheelAngle : -wheelAngle);
        }

        frame.setTransform(ms)
                .translateY(0.2)
                .rotateY(isFacingForward ? 0 : 180);

        drivePin.setTransform(ms)
                .translateY(0.2)
                .scale(1 - 1/512f);

        IBogeyStyle.super.renderSmallInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air, DyeColor paintColour) {

        double offset = 3d / 16d;

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 1, side + (isFacingForward
                            ? -(4d / 16)
                            : (4d / 16)))
                    .rotateX(isFacingForward ? -wheelAngle : wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        ms.translate(0, offset, isFacingForward ? -offset : offset);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_FRAME, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_CONNECTING_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_PIN, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.FOUR_WHEEL_DRIVE_ROD, air)
                .translateZ(isFacingForward ? 0.6 : -0.6)
                .translateY(0.85)
                .rotateY(isFacingForward ? 180 : 0)
                .rotateX(offSetScaleFactor*20-10)
                .translateZ(1/4f * Math.sin(Math.toRadians(wheelAngle)))
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        IBogeyStyle.super.renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air, paintColour);
    }

    @Override
    public void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air, DyeColor dyeColor) {
        CachedBufferer.partial(BogeyPartials.SMALL_FOUR_WHEEL_FRAME, air)
                .translateY(0.2)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.SMALL_FOUR_WHEEL_PIN, air)
                .translateY(0.2)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side + (isFacingForward ? 2 : -2))
                    .rotateX(isFacingForward ? -wheelAngle : wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        IBogeyStyle.super.renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air, dyeColor);
    }

    @Override
    public List<BogeySize> implementedSizes() {
        return Arrays.asList(BogeySize.SMALL, BogeySize.LARGE);
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }
}
