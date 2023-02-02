package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StandardBogeyTileEntity.class)
public class MixinStandardBogeyTileEntity implements IStyledStandardBogeyTileEntity {
    private boolean isFacingForwards = true;
    private int bogeyStyle = 0;
    @Override
    public void setIsFacingForwards(boolean isFacingForwards) {
        this.isFacingForwards = isFacingForwards;
    }

    @Override
    public boolean getIsFacingForwards() {
        return this.isFacingForwards;
    }

    @Override
    public void setBogeyStyle(int bogeyStyle) {
        this.bogeyStyle = bogeyStyle;
    }

    @Override
    public int getBogeyStyle() {
        return this.bogeyStyle;
    }
}
