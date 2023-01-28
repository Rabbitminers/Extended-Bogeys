package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfig;
import com.rabbitminers.extendedbogeys.config.server.ExtendedBogeysServerConfig;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.config.AllConfigs;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(Train.class)
public abstract class MixinTrain {
    @Shadow public List<Carriage> carriages;
    private final AtomicReference<Float> maxSpeed = new AtomicReference<>(AllConfigs.SERVER.trains.trainTopSpeed.getF());
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void onInit(UUID id, UUID owner, TrackGraph graph, List<Carriage> carriages, List<Integer> carriageSpacing, boolean doubleEnded, CallbackInfo ci) {
        carriages.forEach(carriage -> carriage.bogeys.forEach(bogey -> {
            if (bogey != null) {
                int style = ((ICarriageBogeyStyle) bogey).getStyle();
                IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(style);
                maxSpeed.set(Math.min(maxSpeed.get(), bogeyStyle.getMaximumSpeed()));
            }
        }));
    }
    @Inject(method = "maxSpeed", at = @At("RETURN"), remap = false, cancellable = true)
    public void overwriteMaxSpeed(CallbackInfoReturnable<Float> cir) {
        if (ExtendedBogeysConfig.SERVER.shouldApplyMaximumSpeed.get())
            cir.setReturnValue(maxSpeed.get());
    }

    @Inject(method = "burnFuel", at = @At("TAIL"), remap = false)
    public void burnFuel(CallbackInfo ci) {
        carriages.forEach(carriage -> {
            IFluidHandler fluidHandler = carriage.storage.getFluids();
        });
    }
}