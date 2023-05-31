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
    @Shadow
    Direction assemblyDirection;

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
}
