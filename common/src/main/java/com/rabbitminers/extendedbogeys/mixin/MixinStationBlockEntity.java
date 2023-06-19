package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StationBlockEntity.class)
public class MixinStationBlockEntity extends BlockEntity {
    @Shadow
    Direction assemblyDirection;

    public MixinStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    // Would use redirect but mappings seem to have gone wonky :(
    // @Inject(method = "trackClicked", at = @At("TAIL"))
    @Inject(
            method = "trackClicked",
            at = @At(
                    value = "INVOKE",
                    // target = "Lnet/minecraft/world/level/Level;m_7731_(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    public void append(Player player, InteractionHand hand, ITrackBlock track, BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (level == null)
            return;

        BlockPos up = new BlockPos(track.getUpNormal(level, pos, state));
        BlockPos down = new BlockPos(track.getUpNormal(level, pos, state).scale(-1));

        boolean upsideDown = (player.getViewXRot(1.0F) < 0 && (track.getBogeyAnchor(level, pos, state)).getBlock() instanceof AbstractBogeyBlock<?> bogey && bogey.canBeUpsideDown());
        BlockPos targetPos = upsideDown ? pos.offset(down) : pos.offset(up);

        BlockEntity blockEntity = level.getBlockEntity(targetPos);

        if (!(blockEntity instanceof AbstractBogeyBlockEntity bogeyBlockEntity))
            return;

        CompoundTag bogeyData = bogeyBlockEntity.getBogeyData();
        NBTHelper.writeEnum(bogeyData, Constants.BOGEY_ASSEMBLY_DIRECTION_KEY, assemblyDirection);

        bogeyData.putBoolean(Constants.BOGEY_LINK_KEY, true);

        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() instanceof BlockItem blockItem &&
                ExtendedBogeysBlocks.PAINTED_RAILWAY_CASING.contains(blockItem.getBlock()) &&
                blockItem.getBlock() instanceof CasingBlock casingBlock) {
            DyeColor color = ExtendedBogeysBlocks.PAINTED_RAILWAY_CASING.enumValueOfBlock(casingBlock);
            if (color != null)
                NBTHelper.writeEnum(bogeyData, Constants.BOGEY_PAINT_KEY, color);
        }

        bogeyBlockEntity.setBogeyData(bogeyData);
    }

    /*
    @ModifyArg(
            method = "assemble",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/entity/CarriageBogey;<init>(Lcom/simibubi/create/content/trains/bogey/AbstractBogeyBlock;ZLnet/minecraft/nbt/CompoundTag;Lcom/simibubi/create/content/trains/entity/TravellingPoint;Lcom/simibubi/create/content/trains/entity/TravellingPoint;)V"
            ),
            index = 2
    )
    public CompoundTag appendBogeyData(CompoundTag bogeyData) {
        NBTHelper.writeEnum(bogeyData, Constants.BOGEY_ASSEMBLY_DIRECTION_KEY, assemblyDirection);
        return bogeyData;
    }
    */
}
