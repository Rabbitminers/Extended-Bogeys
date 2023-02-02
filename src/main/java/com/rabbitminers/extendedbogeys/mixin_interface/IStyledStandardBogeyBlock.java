package com.rabbitminers.extendedbogeys.mixin_interface;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IStyledStandardBogeyBlock {
    @OnlyIn(Dist.CLIENT)
    default void renderWithTileEntity(BlockState state, IStyledStandardBogeyTileEntity te, float wheelAngle, PoseStack ms,
                                     float partialTicks, MultiBufferSource buffers, int light, int overlay) {

    }
}
