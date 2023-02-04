package com.rabbitminers.extendedbogeys.mixin.client;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.CarriageSyncData;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CarriageContraptionEntity.class)
public class MixinCarriageContraptionEntity implements ICarriageContraptionEntity {
    private double distanceTo = 0;
    @Override
    public double getDistanceTo() {
        return this.distanceTo;
    }

    @ModifyVariable(method = "tickContraption", at = @At("LOAD"), name = "distanceTo",remap = false)
    public double captureDistanceTo(double distanceTo) {
        this.distanceTo = distanceTo;
        return distanceTo;
    }
}
