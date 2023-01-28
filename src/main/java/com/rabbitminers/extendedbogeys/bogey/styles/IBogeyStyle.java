package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public interface IBogeyStyle {
    default int getStyleId() {
        return 0;
    }
    default String getStyleName() {
        return "Invalid Style";
    }

    public default void renderInWorld(boolean isLarge, boolean isFacingForward, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        if (isLarge) renderLargeInWorld(wheelAngle, isFacingForward, ms, light, vb, air); else renderSmallInWorld(wheelAngle, isFacingForward, ms, light, vb, air);
    }

    public default void registerBogeyModelData(boolean isLarge, MaterialManager materialManager) {
        if (isLarge) registerLargeBogeyModelData(materialManager); else registerSmallBogeyModelData(materialManager);
    }

    public default List<GuiGameElement.GuiRenderBuilder> renderInGuiOverlay(boolean isLarge) {
        if (isLarge) return renderLargeInGuiOverlay(); else return renderSmallInGuiOverlay();
    }

    public default void beginFrame(boolean isLarge, boolean isFacingForward, float wheelAngle, PoseStack ms) {
        if (isLarge) renderLargeInContraption(wheelAngle, isFacingForward, ms); else renderSmallInContraption(wheelAngle, isFacingForward, ms);
    }

    default void setEmptyTransforms() {
        for (ModelData customModelComponent : getAllCustomModelComponents())
            customModelComponent.setEmptyTransform();
    }

    default void updateLight(int blockLight, int skyLight) {
        for (ModelData customModelComponent : getAllCustomModelComponents()) {
            assert customModelComponent != null;
            customModelComponent.setSkyLight(skyLight)
                                .setBlockLight(blockLight);
        }
    }

    default void removeAllModelData() {
        for (ModelData customModelComponent : getAllCustomModelComponents())
            customModelComponent.delete();
    }

    default boolean shouldRenderInnerShaft() { return true; }

    default boolean shouldRenderDefault(boolean isLarge) {
        return false;
    }

    default float getBogeyAccelleration() {
        return AllConfigs.SERVER.trains.trainAcceleration.getF();
    }

    default float getMinimumTurnRadius() {
        return 0.0f;
    }

    default float getMaximumSpeed() {
        return AllConfigs.SERVER.trains.trainTopSpeed.getF();
    }

    default List<ModelData> getAllCustomModelComponents() {
        return new ArrayList<>();
    }

    default List<GuiGameElement.GuiRenderBuilder> renderLargeInGuiOverlay() {return new ArrayList<>();}

    default List<GuiGameElement.GuiRenderBuilder> renderSmallInGuiOverlay() {return new ArrayList<>();}

    default void renderLargeInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms) {}

    default void renderSmallInContraption(float wheelAngle, boolean isFacingForward, PoseStack ms) {}

    default void renderLargeInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light, VertexConsumer vb, BlockState air) {}

    default void renderSmallInWorld(float wheelAngle, boolean isFacingForward, PoseStack ms, int light,  VertexConsumer vb, BlockState air) {}

    default void registerLargeBogeyModelData(MaterialManager materialManager) {}

    default void registerSmallBogeyModelData(MaterialManager materialManager) {}
}
