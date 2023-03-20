package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.entity.TrainPacket;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.UUID;

@Mixin(TrainPacket.class)
public class MixinTrainPacket {

    /*
    * TODO: bogey api - (Pass Bogey Data Upwards As NBT Instead Of Seperate Variables As Implemented In Camera
    * Tracking Fork)
     */
    @Shadow private Train train;

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At("TAIL"), remap = false)
    public void readBogeyStylesFromByteBuf(FriendlyByteBuf buffer, CallbackInfo ci, UUID owner, List carriages, List carriageSpacing, int size, boolean doubleEnded) {
        for (Carriage carriage : train.carriages) {
            carriage.bogeys.forEach(bogey -> {
                if (bogey != null) {
                    ((ICarriageBogeyStyle) bogey).setStyle(buffer.readInt());
                    ((ICarriageBogeyStyle) bogey).setFacingForward(buffer.readBoolean());
                    ((ICarriageBogeyStyle) bogey).setAssemblyDirection(buffer.readEnum(Direction.class));
                    ((ICarriageBogeyStyle) bogey).setPaintColour(buffer.readEnum(DyeColor.class));
                }
            });
        }
    }

    @Inject(method = "write", locals = LocalCapture.CAPTURE_FAILHARD, at = @At("TAIL"), remap = false)
    public void writeBogeyStyleToByteBuf(FriendlyByteBuf buffer, CallbackInfo ci) {
        for (Carriage carriage : train.carriages) {
            carriage.bogeys.forEach(bogey -> {
                if (bogey != null) {
                    buffer.writeInt(((ICarriageBogeyStyle) bogey).getStyle());
                    buffer.writeBoolean(((ICarriageBogeyStyle) bogey).isFacingForward());
                    buffer.writeEnum(((ICarriageBogeyStyle) bogey).getAssemblyDirection());
                    buffer.writeEnum(((ICarriageBogeyStyle) bogey).getPaintColour());
                }
            });
        }
    }
}
