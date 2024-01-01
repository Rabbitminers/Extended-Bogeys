package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.base.Constants;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StationBlockEntity.class)
public class MixinStationBlockEntity {
    @Shadow Direction assemblyDirection;

    @ModifyArg(
            method = "trackClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/bogey/AbstractBogeyBlockEntity;setBogeyData(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    public CompoundTag appendAssemblyDirection(CompoundTag bogeyData) {
        NBTHelper.writeEnum(bogeyData, Constants.BOGEY_ASSEMBLY_DIRECTION_KEY, assemblyDirection);

        return bogeyData;
    }
}
