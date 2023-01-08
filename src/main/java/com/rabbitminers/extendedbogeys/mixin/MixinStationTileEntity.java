package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.*;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mixin(StationTileEntity.class)
public class MixinStationTileEntity {

    // TODO: Find a way to limit the captured local variables
    @Inject(
        method = "assemble",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/logistics/trains/entity/Carriage;<init>(Lcom/simibubi/create/content/logistics/trains/entity/CarriageBogey;Lcom/simibubi/create/content/logistics/trains/entity/CarriageBogey;I)V",
            shift = At.Shift.BEFORE
        ),
        remap = false
    )
    public void assembleFirstBogey(UUID playerUUID, CallbackInfo ci, BlockPos trackPosition, BlockState trackState, ITrackBlock track, BlockPos bogeyOffset, TrackNodeLocation location, Vec3 centre, Collection ends, Vec3 targetOffset, List pointOffsets, int iPrevious, List points, Vec3 directionVec, TrackGraph graph, TrackNode secondNode, List contraptions, List carriages, List spacing, boolean atLeastOneForwardControls, int bogeyIndex, int pointIndex, CarriageContraption contraption, BlockPos bogeyPosOffset, IBogeyBlock typeOfFirstBogey, CarriageBogey firstBogey, CarriageBogey secondBogey, BlockPos secondBogeyPos, int bogeySpacing) {
        Level level = Minecraft.getInstance().level;
        assert level != null;

        if (firstBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
            BlockPos firstBogeyBlockPos = contraption.anchor;
            BlockState state = level.getBlockState(firstBogeyBlockPos);
            int firstBogeyStyle = state.getValue(BlockStates.STYLE);
            styledCustomBogey.setStyle(firstBogeyStyle);
        }

        if (secondBogey != null) {
            BlockState secondBogeyState = level.getBlockState(secondBogeyPos);
            int secondBogeyStyle = secondBogeyState.getValue(BlockStates.STYLE);
            if (secondBogey instanceof ICarriageBogeyStyle styledCustomBogey)
                styledCustomBogey.setStyle(secondBogeyStyle);
        }
    }

}
