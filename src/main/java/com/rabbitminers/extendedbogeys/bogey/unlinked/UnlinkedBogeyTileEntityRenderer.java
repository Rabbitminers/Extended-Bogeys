package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.deployer.DeployerRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionInstance;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnlinkedBogeyTileEntityRenderer<T extends BlockEntity> extends SafeTileEntityRenderer<T> {
    public UnlinkedBogeyTileEntityRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(T te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {
        BlockState blockState = te.getBlockState();
        if (blockState.getBlock() instanceof IStyledStandardBogeyBlock bogey &&
                te instanceof IStyledStandardBogeyTileEntity) {
            float angle = 0;

            if (te instanceof UnlinkedBogeyTileEntity ubte)
                angle = ubte.getAngle();

            bogey.renderWithTileEntity(blockState, te, angle, ms, partialTicks, buffer, light, overlay);
        }
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;

        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
        boolean isLarge = state.getBlock() == AllBlocks.LARGE_BOGEY.get();

        float time = AnimationTickHolder.getRenderTime() / 20;
        float angle = (float) time % 360;

        PoseStack ms = matrices.getModel();
        int light = ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld);

        System.out.println(angle);

    }

}
