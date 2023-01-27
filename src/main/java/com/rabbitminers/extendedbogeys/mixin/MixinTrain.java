package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.config.AllConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(Train.class)
public abstract class MixinTrain {
    @Shadow public int fuelTicks;

    @Shadow public List<Carriage> carriages;

    @Shadow public abstract float maxSpeed();

    @Inject(method = "maxSpeed", at = @At("RETURN"), remap = false, cancellable = true)
    public void overwriteMaxSpeed(CallbackInfoReturnable<Float> cir) {
        AtomicReference<Float> maxSpeed = new AtomicReference<>(cir.getReturnValue());

        carriages.forEach(carriage -> carriage.bogeys.forEach(bogey -> {
            int style = ((IBogeyStyle) bogey).getStyleId();
            IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(style);
            if (maxSpeed.get() > bogeyStyle.getMaximumSpeed()) maxSpeed.set(bogeyStyle.getMaximumSpeed());
        }));

        cir.setReturnValue(maxSpeed());
    }
}
