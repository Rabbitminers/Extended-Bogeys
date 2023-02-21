package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.CachedRenderBBTileEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UnlinkedBogeyTileEntity extends CachedRenderBBTileEntity implements
        IStyledStandardBogeyTileEntity, IHaveGoggleInformation {
    private final float wheelRadius = 1;
    LerpedFloat wheelAngle;
    LerpedFloat yaw;
    LerpedFloat pitch;
    public UnlinkedBogeyTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        wheelAngle = LerpedFloat.angular();
        yaw = LerpedFloat.angular();
        pitch = LerpedFloat.angular();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LanguageKey.translate("bogeys.bogey").forGoggles(tooltip);
        if (level == null) return false;
        BlockState state = level.getBlockState(getBlockPos());
        CompoundTag tileData = getTileData();
        int style = getBogeyStyle(tileData);
        IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(style);
        List<BogeySize> implementedSizes = bogeyStyle.implementedSizes();

        tooltip.add(new TranslatableComponent("extendedbogeys.bogeys.sizes.text")
                .withStyle(ChatFormatting.GRAY));

        for (BogeySize value : BogeySize.values()) {
            boolean isImplemented = implementedSizes.contains(value);

            Block valueBlock = ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(value).get();
            boolean isActive = valueBlock == state.getBlock();

            tooltip.add(new TextComponent(isActive ? "-> " : isImplemented ? "✔ " : "× ")
                    .append(new TranslatableComponent(value.getTranslationKey()))
                    .withStyle(isActive ? ChatFormatting.GOLD : isImplemented
                            ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        return true;
    }

    public float getAngle() {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        return this.wheelAngle.getValue(partialTicks);
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
        NBTHelper.writeEnum(tileData, "PaintColour", assemblyDirection);
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
        return  DyeColor.GRAY;
    }

    public void markUpdated() {
        setChanged();
        Level level = getLevel();
        if (level != null) getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public void updateAngles(double distanceMoved) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof IUnlinkedBogeyBlock type))
            return;
        double angleDiff = 360 * distanceMoved / (Math.PI * 2 * type.getWheelRadius());
        double newWheelAngle = (wheelAngle.getValue() - angleDiff) % 360;
        wheelAngle.setValue(newWheelAngle);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    LerpedFloat virtualAnimation = LerpedFloat.angular();

    public float getVirtualAngle(float partialTicks) {
        return virtualAnimation.getValue(partialTicks);
    }

    public void animate(float distanceMoved) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof IUnlinkedBogeyBlock type))
            return;
        double angleDiff = 360 * distanceMoved / (Math.PI * 2 * type.getWheelRadius());
        double newWheelAngle = (virtualAnimation.getValue() - angleDiff) % 360;
        virtualAnimation.setValue(newWheelAngle);
    }
}
