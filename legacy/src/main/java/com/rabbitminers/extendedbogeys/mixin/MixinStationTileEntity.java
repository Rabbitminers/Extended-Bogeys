package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.BogeySizeUtils;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.ITrackBlock;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.management.edgePoint.TrackTargetingBehaviour;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.GlobalStation;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.WorldAttached;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(StationTileEntity.class)
public abstract class MixinStationTileEntity extends BlockEntity {
    @Shadow public abstract void refreshAssemblyInfo();

    @Shadow public static WorldAttached<Map<BlockPos, BoundingBox>> assemblyAreas;

    @Shadow public abstract boolean isValidBogeyOffset(int i);

    @Shadow public TrackTargetingBehaviour<GlobalStation> edgePoint;
    @Shadow private Direction assemblyDirection;
    private CarriageContraption contraption;

    private DyeColor oldDyeColour;
    private int oldStyle;
    private boolean oldIsFacingForwards;

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

    // Pass up bogey style, & pass styles if it is not implemented
    @Inject(method = "trackClicked", at = @At("HEAD"), cancellable = true, remap = false)
    public void transferBogeySize(Player player, InteractionHand hand, ITrackBlock track, BlockState state, BlockPos pos,
                                  CallbackInfoReturnable<Boolean> cir) {
        refreshAssemblyInfo();

        BoundingBox bb = assemblyAreas.get(level)
                .get(worldPosition);

        if (bb == null || !bb.isInside(pos))
            return;

        BlockPos up = new BlockPos(track.getUpNormal(level, pos, state));
        int bogeyOffset = pos.distManhattan(edgePoint.getGlobalPosition()) - 1;

        if (!isValidBogeyOffset(bogeyOffset)) {
            for (int i = -1; i <= 1; i++) {
                BlockPos bogeyPos = pos.relative(assemblyDirection, i)
                        .offset(up);
                BlockState blockState = level.getBlockState(bogeyPos);
                if (blockState.getBlock() instanceof IBogeyBlock bogey) {
                    captureBogeyStyleInformation(player, bogeyPos);
                    IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(oldStyle);
                    List<Block> blocks = bogeyStyle.implementedSizes()
                            .stream()
                            .map(size -> BogeySizeUtils.blockStateFromBogeySize(size)
                                    .getBlock())
                            .toList();

                    BlockState newBlockState = blockState;
                    for (int j = 0; j <= BogeySize.values().length; j++) {
                        newBlockState = bogey.getRotatedBlockState(newBlockState, Direction.DOWN);
                        if (blocks.contains(newBlockState.getBlock()))
                            break;
                    }

                    level.setBlock(bogeyPos, newBlockState, 3);
                    setBogeyStyleInformation(player, bogeyPos);

                    if (newBlockState.getBlock() == blockState.getBlock())
                        player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.no_change")
                                .withStyle(ChatFormatting.RED), true);

                    bogey.playRotateSound(level, bogeyPos);

                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }

    // Done for a 2am hotfix - fix it
    public void captureBogeyStyleInformation(Player player, BlockPos bogeyPos) {
        player.displayClientMessage(new TextComponent("Set Bogey Size As: "), true);

        if (level == null)
            return;

        BlockEntity be = level.getBlockEntity(bogeyPos);
        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) be;

        CompoundTag tileData = be.getTileData();
        oldDyeColour = te.getPaintColour(tileData);
        oldStyle = te.getBogeyStyle(tileData);
        oldIsFacingForwards = te.getIsFacingForwards(tileData);
    }

    public void setBogeyStyleInformation(Player player, BlockPos bogeyPos) {
        BlockEntity newBlockEntity = level.getBlockEntity(bogeyPos);
        if (newBlockEntity == null) return;

        IStyledStandardBogeyTileEntity newTileEntity = (IStyledStandardBogeyTileEntity) newBlockEntity;
        CompoundTag newTileData = newBlockEntity.getTileData();

        newTileEntity.setBogeyStyle(newTileData, oldStyle);
        newTileEntity.setIsFacingForwards(newTileData, oldIsFacingForwards);
        newTileEntity.setPaintColour(newTileData, oldDyeColour);

        Block newBlock = level.getBlockState(bogeyPos).getBlock();

        if (newBlock instanceof IStyledStandardBogeyBlock ssbb) {
            BogeySize size = ssbb.getBogeySize();
            player.displayClientMessage(new TextComponent("Set Bogey Size As: ")
                    .append(new TranslatableComponent(size.getTranslationKey())), true);
        }
    }
}
