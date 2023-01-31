package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.contraptions.components.actors.DrillRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionMatrices;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class UnlinkedBogeyCarriageMovementBehaviour implements MovementBehaviour {
    @Override
    public boolean renderAsNormalTileEntity() {
        return true;
    }

    private UnlinkedBogeyTileEntity getTileEntity(MovementContext context) {
        Map<BlockPos, BlockEntity> tes = context.contraption.presentTileEntities;
        if (!(tes.get(context.localPos) instanceof UnlinkedBogeyTileEntity unlinkedBogeyTileEntity))
            return null;
        return unlinkedBogeyTileEntity;
    }


    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!context.world.isClientSide || !isActive(context))
            return;

        UnlinkedBogeyTileEntity unlinkedBogeyTileEntity = getTileEntity(context);

        if (!(context.contraption.entity instanceof CarriageContraptionEntity cce) || unlinkedBogeyTileEntity == null)
            return;

        double distanceTo = 0;

        if (cce instanceof ICarriageContraptionEntity carriageContraptionEntityInterface)
            distanceTo = carriageContraptionEntityInterface.getDistanceTo();

        unlinkedBogeyTileEntity.updateAngles(cce, distanceTo);

        MovementBehaviour.super.tick(context);
    }
}
