package com.rabbitminers.extendedbogeys.bogey.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public interface IBogeyStyleRenderer {
    public boolean[] implementedSizes = {false, false, false, false, false};

    @OnlyIn(Dist.CLIENT)
    public List<ModelData> getAllCustomModelComponents();

    @OnlyIn(Dist.CLIENT)
    public void renderInWorld(BogeySize size, boolean isLarge, boolean isFacingForward, float wheelAngle, PoseStack ms,
                               int light, VertexConsumer vb, BlockState air, DyeColor dyeColor);
    @OnlyIn(Dist.CLIENT)
    public void registerBogeyModelData(BogeySize size, MaterialManager materialManager, DyeColor dyeColor);

    @OnlyIn(Dist.CLIENT)
    public void renderInContraption(BogeySize size, boolean isFacingForward, float wheelAngle, PoseStack ms,
                                    Direction assemblyDirection);
    @Deprecated
    default List<GuiGameElement.GuiRenderBuilder> renderInGuiOverlay(boolean isLarge) {
        return new ArrayList<>();
    }

    default void setEmptyTransforms() {
        for (ModelData customModelComponent : getAllCustomModelComponents()) customModelComponent.setEmptyTransform();
    }

    default void updateLight(int blockLight, int skyLight) {
        for (ModelData customModelComponent : getAllCustomModelComponents()) {
            assert customModelComponent != null;
            customModelComponent.setSkyLight(skyLight).setBlockLight(blockLight);
        }
    }

    @OnlyIn(Dist.CLIENT)
    default boolean shouldRenderDefault(boolean isLarge) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    default boolean shouldRenderInnerShaft() { return true; }
}
