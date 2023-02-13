package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

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
            DyeColor firstBogeyColour = firstBogeyTe.getPaintColour(tileData);

            styledCustomBogey.setAssemblyDirection(contraption.getAssemblyDirection());
            styledCustomBogey.setStyle(firstBogeyStyle);
            styledCustomBogey.setFacingForward(isFirstBogeyFacingForward);
            styledCustomBogey.setPaintColour(firstBogeyColour);

            return (CarriageBogey) styledCustomBogey;
        }

        return firstBogey;
    }

    @ModifyVariable(method = "assemble", at = @At("STORE"), name = "secondBogey", remap = false)
    public CarriageBogey onSecondBogeyInit(CarriageBogey secondBogey) {
        if (contraption == null || level == null)
            return secondBogey;

        if (secondBogey != null) {
            BlockPos secondBogeyPos = contraption.getSecondBogeyPos();

            if (!(level.getBlockEntity(secondBogeyPos) instanceof IStyledStandardBogeyTileEntity secondBogeyTe))
                return secondBogey;

            CompoundTag tileData = ((BlockEntity) secondBogeyTe).getTileData();

            int secondBogeyStyle = secondBogeyTe.getBogeyStyle(tileData);
            boolean isSecondBogeyFacingForward = secondBogeyTe.getIsFacingForwards(tileData);
            DyeColor secondBogeyDyeColour = secondBogeyTe.getPaintColour(tileData);

            if (secondBogey instanceof ICarriageBogeyStyle styledCustomBogey) {
                styledCustomBogey.setAssemblyDirection(contraption.getAssemblyDirection());
                styledCustomBogey.setStyle(secondBogeyStyle);
                styledCustomBogey.setFacingForward(isSecondBogeyFacingForward);
                styledCustomBogey.setPaintColour(secondBogeyDyeColour);
                return (CarriageBogey) styledCustomBogey;
            }
        }

        return secondBogey;
    }

    @Redirect(
            method = "trackClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
                    ordinal = 0
            ),
            remap = false
    )
    public boolean passBogeyDataToNewSize(Level instance, BlockPos bogeyPos, BlockState blockState, int someNumber) {
        if (!(blockState.getBlock() instanceof IBogeyBlock bogey) || level == null)
            return false;
        BlockEntity be = level.getBlockEntity(bogeyPos);
        if (be == null) return false;
        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) be;
        CompoundTag tileData = be.getTileData();

        int oldStyle = te.getBogeyStyle(tileData);
        boolean oldIsFacingForwards = te.getIsFacingForwards(tileData);
        DyeColor oldPaintColour = te.getPaintColour(tileData);

        boolean returnValue = level.setBlock(bogeyPos, bogey.getRotatedBlockState(blockState, Direction.DOWN), 3);

        BlockEntity newBlockEntity = level.getBlockEntity(bogeyPos);
        if (newBlockEntity == null) return false;
        IStyledStandardBogeyTileEntity newTileEntity = (IStyledStandardBogeyTileEntity) newBlockEntity;
        CompoundTag newTileData = newBlockEntity.getTileData();

        newTileEntity.setBogeyStyle(newTileData, oldStyle);
        newTileEntity.setIsFacingForwards(newTileData, oldIsFacingForwards);
        newTileEntity.setPaintColour(newTileData, oldPaintColour);

        return returnValue;
    }
}
