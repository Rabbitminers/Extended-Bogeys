package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionMatrices;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnlinkedBogeyTileEntityRenderer<T extends BlockEntity> extends SafeTileEntityRenderer<T> {
    public UnlinkedBogeyTileEntityRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(T te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {
        BlockState blockState = te.getBlockState();
        float angle = 0;

        if (te instanceof UnlinkedBogeyTileEntity sbte)
            angle = sbte.getVirtualAngle(partialTicks);

        if (blockState.getBlock() instanceof IUnlinkedBogeyBlock bogey)
            bogey.render(blockState, angle, ms, partialTicks, buffer, light, overlay);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;
        boolean isLarge = state.getBlock() == AllBlocks.LARGE_BOGEY.get();

        float time = AnimationTickHolder.getRenderTime() / 20;
        float angle = (float) time % 360;

        int style = state.getValue(BlockStates.STYLE);
        IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(style);
    }

}
