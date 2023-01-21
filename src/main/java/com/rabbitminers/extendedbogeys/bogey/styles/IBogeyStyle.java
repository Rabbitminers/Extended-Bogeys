package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockPartials;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.meta.When;
import java.util.ArrayList;
import java.util.List;

public interface IBogeyStyle {
    default int getStyleId() {
        return 0;
    }
    default String getStyleName() {
        return "Invalid Style";
    }
    public default void renderInWorld(boolean isLarge, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        if (isLarge) renderLargeInWorld(wheelAngle, ms, light, vb, air); else renderSmallInWorld(wheelAngle, ms, light, vb, air);
    }
    public default void renderInContraption(boolean isLarge, MaterialManager materialManager) {
        if (isLarge) renderLargeInContraption(materialManager); else renderSmallInContraption(materialManager);
    }
    public default void beginFrame(boolean isLarge, float wheelAngle, PoseStack ms) {
        if (isLarge) beginFrameLarge(wheelAngle, ms); else beginFrameSmall(wheelAngle, ms);
    }
    default void setEmptyTransforms() {
        for (ModelData customModelComponent : getAllCustomModelComponents())
            customModelComponent.setEmptyTransform();
    }
    default void updateLight(int blockLight, int skyLight) {
        for (ModelData customModelComponent : getAllCustomModelComponents())
            customModelComponent
                    .setSkyLight(skyLight)
                    .setBlockLight(blockLight);
    }
    default void removeAllModelData() {
        for (ModelData customModelComponent : getAllCustomModelComponents())
            customModelComponent.delete();
    }
    default boolean shouldRenderDefault(boolean isLarge) {
        return false;
    }
    default float getMinimumTurnRadius() {
        return 0.0f;
    }
    default List<ModelData> getAllCustomModelComponents() {
        return new ArrayList<>();
    }
    default void beginFrameLarge(float wheelAngle, PoseStack ms) {}
    default void beginFrameSmall(float wheelAngle, PoseStack ms) {}
    default void renderLargeInWorld(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {}
    default void renderSmallInWorld(float wheelAngle, PoseStack ms, int light,  VertexConsumer vb, BlockState air) {}
    default void renderLargeInContraption(MaterialManager materialManager) {}
    default void renderSmallInContraption(MaterialManager materialManager) {}
}
