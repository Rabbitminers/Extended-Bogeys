package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock.AXIS;

@Mixin(StandardBogeyBlock.class)
@OnlyIn(Dist.CLIENT)
public class MixinBogeyRender {

    private void renderCustomBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BLAZE_ACTIVE, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.BLAZE_BURNER_FLAME, air)
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }
    }
    @Inject(at = @At("HEAD"), method = "render", cancellable = true, remap = false)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay, CallbackInfo ci) {
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        ms.translate(0, -1.5 - 1 / 128f, 0);

        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());
        BlockState air = Blocks.AIR.defaultBlockState();

        for (int i : Iterate.zeroAndOne)
            CachedBufferer.block(AllBlocks.SHAFT.getDefaultState()
                            .setValue(ShaftBlock.AXIS, Direction.Axis.Z))
                    .translate(-.5f, .25f, i * -1)
                    .centre()
                    .rotateZ(wheelAngle)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);

        renderCustomBogey(wheelAngle, ms, light, vb, air);

        ci.cancel();
    }
}
