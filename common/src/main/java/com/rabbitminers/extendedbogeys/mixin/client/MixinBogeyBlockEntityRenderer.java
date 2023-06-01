package com.rabbitminers.extendedbogeys.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogeys.unlinked.UnlinkedBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.bogey.BogeyBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyBlockEntityRenderer.class)
public class MixinBogeyBlockEntityRenderer<T extends BlockEntity> {
    @Inject(
            method = "renderSafe",
            at = @At("TAIL")
    )
    public void renderUnlinkedBogey(T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        BlockState blockState = be.getBlockState();
        if (be instanceof AbstractBogeyBlockEntity sbte) {
            float angle = sbte.getVirtualAngle(partialTicks);
            if (blockState.getBlock() instanceof UnlinkedBogeyBlock bogey) {
                bogey.render(blockState, angle, ms, partialTicks, buffer, light, overlay, sbte.getStyle(), sbte.getBogeyData());
            }
        }
    }
}
