package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.simibubi.create.content.contraptions.components.actors.DrillRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

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
    public void tick(MovementContext context) {
        if (!context.world.isClientSide || !isActive(context))
            return;

        UnlinkedBogeyTileEntity unlinkedBogeyTileEntity = getTileEntity(context);

        float speed = context.getAnimationSpeed();
        float time = AnimationTickHolder.getRenderTime() / 20;
        float angle = (float) (((time * speed) % 360));

        unlinkedBogeyTileEntity.setRotationAngle(angle);

        MovementBehaviour.super.tick(context);
    }
}
