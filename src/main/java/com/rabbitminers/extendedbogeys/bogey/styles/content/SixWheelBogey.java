package com.rabbitminers.extendedbogeys.bogey.styles.content;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;
import com.rabbitminers.extendedbogeys.bogey.util.RotationUtils;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SixWheelBogey implements IBogeyStyle {

    private ModelData frame;
    private ModelData[] wheels;
    private ModelData connectingRod;
    private ModelData drivePin;
    private ModelData driveRod;
    private ModelData radiusRod;
    private ModelData eccentric;
    private ModelData eccentricRod;

    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.sixwheel").getString();
    }

    @Override
    public List<ModelData> getAllCustomModelComponents() {
        List<ModelData> modelData = new ArrayList<>();
        modelData.add(frame);
        modelData.addAll(List.of(wheels));
        modelData.add(connectingRod);
        modelData.add(drivePin);
        modelData.add(driveRod);
        modelData.add(radiusRod);
        modelData.add(eccentric);
        modelData.add(eccentricRod);
        return modelData;
    }

    @Override
    public void registerSmallBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
        /*
        Placeholder
         */

        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_FRAME)
                .createInstance();

        wheels = new ModelData[3];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.SMALL_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_CONNECTING_ROD)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_DRIVE_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_DRIVE_ROD)
                .createInstance();

        radiusRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_RADIUSROD)
                .createInstance();

        eccentric = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_ECCENTRIC)
                .createInstance();

        eccentricRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_ECCENTRIC_ROD)
                .createInstance();
    }

    @Override
    public void registerLargeBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
        frame = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_FRAME)
                .createInstance();

        wheels = new ModelData[3];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.LARGE_BOGEY_WHEELS)
                .createInstances(wheels);

        connectingRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_CONNECTING_ROD)
                .createInstance();

        drivePin = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_DRIVE_PIN)
                .createInstance();

        driveRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_DRIVE_ROD)
                .createInstance();

        radiusRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_RADIUSROD)
                .createInstance();

        eccentric = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_ECCENTRIC)
                .createInstance();

        eccentricRod = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.SIX_WHEEL_ECCENTRIC_ROD)
                .createInstance();
    }

    @Override
    public void renderLargeInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms, Direction assemblyDirection) {
        boolean isDirectionPosotive = RotationUtils.isDirectionPosotive(assemblyDirection);
        isFacingForward = isDirectionPosotive == isFacingForward;
        wheelAngle = isFacingForward ? wheelAngle : -wheelAngle;

        double offset = 3d / 16d;

        for (int side = -1; side < 2; side++) {
            wheels[side + 1].setTransform(ms)
                    .translate(0, 1, side * 1.875)
                    .rotateX(isFacingForward ? -wheelAngle : wheelAngle);
        }

        // The
        ms.translate(0, offset, isFacingForward ? offset : -offset);

        frame.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .scale(1 - 1/512f);

        connectingRod.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .translateY(-4d/16d)
                .scale(1 - 1/512f);

        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));
        double zOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

        driveRod.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .translate(0, 0.9, -1.875)
                .rotateX(8.5*offSetScaleFactor)
                .translateZ(zOffset)
                .scale(1 - 1/512f);

        drivePin.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .translateZ(zOffset)
                .scale(1 - 1/512f);

        eccentric.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .translate(0, 0.8, 1.68)
                .rotateX(isFacingForward ? -wheelAngle : wheelAngle)
                .scale(1 - 1/512f);

        eccentricRod.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .scale(1 - 1/512f);

        radiusRod.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .scale(1 - 1/512f);

        IBogeyStyle.super.renderLargeInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb,
                                   BlockState air, DyeColor paintColour) {
        double offset = 3d / 16d;

        for (int side = -1; side < 2; side++) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                    .translate(0, 1, side * 1.875)
                    .rotateX(isFacingForward ? -wheelAngle : wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        ms.translate(0, offset, isFacingForward ? -offset : offset);

        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_FRAME, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_CONNECTING_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .scale(1 - 1/512f)
                .translateY(-4d/16d)
                .light(light)
                .renderInto(ms, vb);

        float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));
        double zOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_DRIVE_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translate(0, 0.9, -1.875)
                .rotateX(8.5*offSetScaleFactor)
                .translateZ(zOffset)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_DRIVE_PIN, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translateZ(zOffset)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_ECCENTRIC, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translate(0, 0.8, 1.68)
                .rotateX(isFacingForward ? -wheelAngle : wheelAngle)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_ECCENTRIC_ROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        CachedBufferer.partial(BogeyPartials.SIX_WHEEL_RADIUSROD, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);


        IBogeyStyle.super.renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air, paintColour);
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }
}
