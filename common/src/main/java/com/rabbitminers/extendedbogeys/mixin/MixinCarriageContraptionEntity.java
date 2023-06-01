package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CarriageContraptionEntity.class)
public class MixinCarriageContraptionEntity implements ICarriageContraptionEntity {
    @Shadow private Carriage carriage;
    private double distanceTo = 0;

    @ModifyVariable(method = "tickContraption", at = @At("LOAD"), name = "distanceTo",remap = false)
    public double captureDistanceTo(double distanceTo) {
        this.distanceTo = distanceTo;
        return distanceTo;
    }

    @Override
    public double getDistanceTo() {
        return -distanceTo;
    }
}

