package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(StandardBogeyTileEntity.class)
public class MixinStandardBogeyTileEntity extends BlockEntity implements
        IStyledStandardBogeyTileEntity, IHaveGoggleInformation {
    public MixinStandardBogeyTileEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LanguageKey.translate("bogeys.bogey").forGoggles(tooltip);
        if (level == null) return false;
        BlockState state = level.getBlockState(getBlockPos());
        CompoundTag tileData = getTileData();
        int style = getBogeyStyle(tileData);

        tooltip.add(new TranslatableComponent("extendedbogeys.bogeys.sizes.text")
            .withStyle(ChatFormatting.GRAY));

        for (BogeySize value : BogeySize.values()) {
            boolean isImplemented = true;

            Block valueBlock = switch (value) {
                case SMALL -> AllBlocks.SMALL_BOGEY.get();
                case LARGE -> AllBlocks.LARGE_BOGEY.get();
                default -> ExtendedBogeysBlocks.CUSTOM_BOGEYS.get(value).get();
            };

            boolean isActive = valueBlock == state.getBlock();

            tooltip.add(new TextComponent(isActive ? "-> " : isImplemented ? "✔ " : "× ")
                    .append(new TranslatableComponent(value.getTranslationKey()))
                    .withStyle(isActive ? ChatFormatting.GOLD : isImplemented
                            ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setIsFacingForwards(CompoundTag tileData, boolean isFacingForwards) {
        tileData.putBoolean("IsFacingForwards", isFacingForwards);
        markUpdated();
    }

    @Override
    public boolean getIsFacingForwards(CompoundTag tileData) {
        if (tileData.contains("IsFacingForwards"))
            return tileData.getBoolean("IsFacingForwards");
        return true;
    }

    @Override
    public void setBogeyStyle(CompoundTag tileData, int bogeyStyle) {
        tileData.putInt("Style", bogeyStyle);
        markUpdated();
    }

    @Override
    public int getBogeyStyle(CompoundTag tileData) {
        if (tileData.contains("Style"))
            return tileData.getInt("Style");
        return 0;
    }

    @Override
    public void setAssemblyDirection(CompoundTag tileData, Direction assemblyDirection) {
        NBTHelper.writeEnum(tileData, "AssemblyDirection", assemblyDirection);
        markUpdated();
    }

    @Override
    public Direction getAssemblyDirection(CompoundTag tileData) {
        if (tileData.contains("AssemblyDirection"))
            return NBTHelper.readEnum(tileData, "AssemblyDirection", Direction.class);
        return null;
    }

    @Override
    public void setPaintColour(CompoundTag tileData, DyeColor colour) {
        NBTHelper.writeEnum(tileData, "PaintColour", colour);
        markUpdated();
    }

    @Override
    public DyeColor getPaintColour(CompoundTag tileData) {
        if (tileData.contains("PaintColour"))
            return NBTHelper.readEnum(tileData, "PaintColour", DyeColor.class);
        return DyeColor.GRAY;
    }

    private void markUpdated() {
        setChanged();
        Level level = getLevel();
        if (level != null) getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

}
