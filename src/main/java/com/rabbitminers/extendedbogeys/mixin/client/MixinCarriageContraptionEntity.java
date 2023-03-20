package com.rabbitminers.extendedbogeys.mixin.client;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CarriageContraptionEntity.class)
public class MixinCarriageContraptionEntity implements ICarriageContraptionEntity {
    /*
    TODO: Bogey Api
    Allow for distance to next point to be accessed publicly
    (Unless it is already I missed it)
     */

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
