package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfig;
import com.rabbitminers.extendedbogeys.config.server.ExtendedBogeysServerConfig;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.logistics.trains.DimensionPalette;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(Train.class)
public abstract class MixinTrain {
    @Shadow public List<Carriage> carriages;
    @Shadow public boolean currentlyBackwards;

    @Shadow public abstract void earlyTick(Level level);
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

    @Inject(method="acceleration", at = @At("RETURN"), remap = false)
    public void modifyAccelleration(CallbackInfoReturnable<Float> cir) {
        float defaultAccelleration = cir.getReturnValue();
    }


    @Inject(
            method = "disassemble",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/trains/entity/CarriageContraptionEntity;disassemble()V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    public void onDisassemble(Direction assemblyDirection, BlockPos pos, CallbackInfoReturnable<Boolean> cir, int offset, boolean backwards, Level level, int i, Carriage carriage, CarriageContraptionEntity entity) {
        for (CarriageBogey bogey : carriage.bogeys) {
            if (bogey instanceof ICarriageBogeyStyle styledCarriageBogey) {
                Vec3 bogeyPos = bogey.getAnchorPosition();
                if (bogeyPos == null) return;

                DyeColor paintColor = styledCarriageBogey.getPaintColour();
                int style = styledCarriageBogey.getStyle();
                boolean isFacingForwards = styledCarriageBogey.isFacingForward();

                BlockEntity be = level.getBlockEntity
                        (new BlockPos(bogeyPos.x, bogeyPos.y, bogeyPos.z));
                System.out.println(be);

                if (!(be instanceof IStyledStandardBogeyTileEntity ssbte))
                    return;

                CompoundTag tileData = be.getTileData();
                ssbte.setIsFacingForwards(tileData, isFacingForwards);
                ssbte.setBogeyStyle(tileData, style);
                ssbte.setAssemblyDirection(tileData, assemblyDirection);
                ssbte.setPaintColour(tileData, paintColor);
            }
        }
    }
}
