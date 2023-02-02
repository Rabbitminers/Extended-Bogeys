package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.logistics.trains.BogeyTileEntityRenderer;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyTileEntityRenderer.class)
public class MixinBogeyTileEntityRenderer {
    @Inject(
        method = "renderSafe",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    public <T extends BlockEntity> void renderWithTileEntity(T te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                                             int light, int overlay, CallbackInfo ci) {
        BlockState blockState = te.getBlockState();
        if (te instanceof IStyledStandardBogeyTileEntity ssbte
                && blockState.getBlock() instanceof IStyledStandardBogeyBlock bogey) {
            float angle = 0;
            if (te instanceof StandardBogeyTileEntity sbte)
                angle = sbte.getVirtualAngle(partialTicks);
            bogey.renderWithTileEntity(blockState, ssbte, angle, ms, partialTicks, buffer, light, overlay);
            ci.cancel();
        }
    }
}
