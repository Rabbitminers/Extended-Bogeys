package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.logistics.trains.*;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mixin(StationTileEntity.class)
public class MixinStationTileEntity extends BlockEntity {
    private CarriageContraption contraption;

    public MixinStationTileEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "contraption", remap = false)
    public CarriageContraption captureCarriageContraptionOnInit(CarriageContraption carriageContraption) {
        this.contraption = carriageContraption;
        return carriageContraption;
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "firstBogey", remap = false)
    public CarriageBogey onFirstBogeyInit(CarriageBogey firstBogey) {
        if (contraption == null || level == null)
            return firstBogey;

        if (firstBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
            BlockPos firstBogeyBlockPos = contraption.anchor;

            IStyledStandardBogeyTileEntity firstBogeyTe = (IStyledStandardBogeyTileEntity) level.getBlockEntity(firstBogeyBlockPos);
            assert firstBogeyTe != null;

            CompoundTag tileData = ((BlockEntity) firstBogeyTe).getTileData();

            boolean isFirstBogeyFacingForward = firstBogeyTe.getIsFacingForwards(tileData);
            int firstBogeyStyle = firstBogeyTe.getBogeyStyle(tileData);

            styledCustomBogey.setAssemblyDirection(contraption.getAssemblyDirection());
            styledCustomBogey.setStyle(firstBogeyStyle);
            styledCustomBogey.setFacingForward(isFirstBogeyFacingForward);

            return (CarriageBogey) styledCustomBogey;
        }

        return firstBogey;
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "secondBogey", remap = false)
    public CarriageBogey onSecondBogeyInit(CarriageBogey secondBogey) {
        if (contraption == null || level == null)
            return secondBogey;

        BlockPos secondBogeyPos = contraption.getSecondBogeyPos();
        if (level.getBlockEntity(secondBogeyPos) instanceof IStyledStandardBogeyTileEntity secondBogeyTe
                && secondBogey != null) {

            CompoundTag tileData = ((BlockEntity) secondBogeyTe).getTileData();

            int secondBogeyStyle = secondBogeyTe.getBogeyStyle(tileData);
            boolean isSecondBogeyFacingForward = secondBogeyTe.getIsFacingForwards(tileData);

            if (secondBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
                styledCustomBogey.setAssemblyDirection(contraption.getAssemblyDirection());
                styledCustomBogey.setStyle(secondBogeyStyle);
                styledCustomBogey.setFacingForward(isSecondBogeyFacingForward);
                return (CarriageBogey) styledCustomBogey;
            }
        }

        return secondBogey;
    }
}
