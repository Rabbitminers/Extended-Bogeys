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
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleAxisBogey implements IBogeyStyle {
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
    public void registerSmallBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
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
    public void registerLargeBogeyModelData(MaterialManager materialManager, DyeColor paintColour) {
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
    public void renderSmallInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms, Direction assemblyDirection) {
        boolean isDirectionPosotive = RotationUtils.isDirectionPosotive(assemblyDirection);
        isFacingForward = isDirectionPosotive == isFacingForward;
        wheelAngle = isFacingForward ? wheelAngle : -wheelAngle;

        wheels.setTransform(ms)
                .rotateY(isFacingForward ? 0 : 180)
                .translate(0, 12 / 16f, -1)
                .rotateX(wheelAngle);

        frame.setTransform(ms).rotateY(isFacingForward ? 0 : 180).translate(0, 0.1, 0);

        pin.setTransform(ms).rotateY(isFacingForward ? 0 : 180);

        IBogeyStyle.super.renderSmallInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
    }

    @Override
    public void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air, DyeColor paintColour) {
        IBogeyStyle.super.renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air, paintColour);
    }
    @Override
    public void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air, DyeColor dyeColor) {
        CachedBufferer.partial(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translate(0, 0.1, 0)
                .scale(1 - 1/512f)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(BogeyPartials.SINGLE_AXEL_LEADING_TRUCK_PIN, air)
                .rotateY(isFacingForward ? 180 : 0)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        ms.pushPose();

        CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                .rotateY(isFacingForward ? 180 : 0)
                .translate(0, 12 / 16f, -1)
                .rotateX(wheelAngle)
                .light(light)
                .renderInto(ms, vb);

        ms.popPose();
    }

    @Override
    public List<BogeySize> implemntedSizes() {
        return List.of(BogeySize.LARGE);
    }

    @Override
    public float getMaximumSpeed() {
        return 16;
    }

    @Override
    public boolean shouldRenderInnerShaft() {
        return false;
    }

    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.singleaxis").getString();
    }
}
