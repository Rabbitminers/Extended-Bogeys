package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageSyncData;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CarriageContraptionEntity.class)
public class MixinCarriageContraptionEntity implements ICarriageContraptionEntity {
    @Shadow private Carriage carriage;
    private double distanceTo = 0;

    @Inject(method = "tickContraption", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "TAIL"), remap = false)
    public void capture(CallbackInfo ci, CarriageContraption cc, boolean isStalled, CarriageSyncData carriageData,
                        Carriage.DimensionalCarriageEntity dce, double distanceTo) {
        Direction assemblyDirection = cc.getAssemblyDirection();
        this.distanceTo = isDirectionPosotive(assemblyDirection) ? distanceTo : -distanceTo;
    }

    @Override
    public double getDistanceTo() {
        return -distanceTo;
    }

    private static boolean isDirectionPosotive(Direction direction) {
        return switch (direction) { case NORTH, WEST, UP -> true; case SOUTH, DOWN, EAST -> false; };
    }
}

