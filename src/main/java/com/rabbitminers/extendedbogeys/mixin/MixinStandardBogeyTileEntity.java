package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StandardBogeyTileEntity.class)
public class MixinStandardBogeyTileEntity implements IStyledStandardBogeyTileEntity {
    @Override
    public void setIsFacingForwards(CompoundTag tileData, boolean isFacingForwards) {
        tileData.putBoolean("IsFacingForwards", isFacingForwards);
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
    }

    @Override
    public int getBogeyStyle(CompoundTag tileData) {
        if (tileData.contains("Style"))
            return tileData.getInt("Style");
        return 0;
    }
}
