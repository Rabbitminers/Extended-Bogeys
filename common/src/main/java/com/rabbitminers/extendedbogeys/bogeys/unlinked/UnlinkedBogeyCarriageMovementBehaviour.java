package com.rabbitminers.extendedbogeys.bogeys.unlinked;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.rabbitminers.extendedbogeys.multiloader.CarriageContraptionHelper;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.trains.bogey.StandardBogeyBlockEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;

public class UnlinkedBogeyCarriageMovementBehaviour implements MovementBehaviour {
    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }

    private StandardBogeyBlockEntity getTileEntity(MovementContext context) {
        Map<BlockPos, BlockEntity> tes = context.contraption.presentBlockEntities;
        if (!(tes.get(context.localPos) instanceof StandardBogeyBlockEntity unlinkedBogeyTileEntity))
            return null;
        return unlinkedBogeyTileEntity;
    }

    @Override
    public void tick(MovementContext context) {
        if (!context.world.isClientSide || !isActive(context))
            return;

        StandardBogeyBlockEntity unlinkedBogeyTileEntity = getTileEntity(context);

        if (!(context.contraption.entity instanceof CarriageContraptionEntity cce) || unlinkedBogeyTileEntity == null)
            return;

        if (Minecraft.getInstance().isPaused())
            return;

        double distanceTo = CarriageContraptionHelper.getDistanceTo(cce);
        unlinkedBogeyTileEntity.animate((float) distanceTo);
    }
}
