package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface IUnlinkedBogeyBlock extends IWrenchable {
    @OnlyIn(Dist.CLIENT)
    void render(@Nullable BlockState state, float wheelAngle, PoseStack ms, float partialTicks,
                MultiBufferSource buffers, int light, int overlay);

    default float getWheelRadius() { return 1f; }
}
