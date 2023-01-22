package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.entity.TrainPacket;
import com.simibubi.create.content.logistics.trains.track.TrackInstance;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.network.FriendlyByteBuf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Mixin(TrainPacket.class)
public class MixinTrainPacket {
    @Shadow private Train train;

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At("TAIL"), remap = false)
    public void readBogeyStylesFromByteBuf(FriendlyByteBuf buffer, CallbackInfo ci, UUID owner, List carriages, List carriageSpacing, int size, boolean doubleEnded) {
        for (Carriage carriage : train.carriages) {
            carriage.bogeys.forEach(bogey -> {
                int bufValue = buffer.readInt();
                System.out.println("Reading from packet: " + bufValue);
                ((ICarriageBogeyStyle) bogey).setStyle(bufValue);
            });
        }
    }

    @Inject(method = "write", locals = LocalCapture.CAPTURE_FAILHARD, at = @At("TAIL"), remap = false)
    public void writeBogeyStyleToByteBuf(FriendlyByteBuf buffer, CallbackInfo ci) {
        for (Carriage carriage : train.carriages) {
            carriage.bogeys.forEach(bogey -> {
                System.out.println("Writing to packet: " + ((ICarriageBogeyStyle) bogey).getStyle());
                buffer.writeInt(((ICarriageBogeyStyle) bogey).getStyle());
            });
        }
    }
}
