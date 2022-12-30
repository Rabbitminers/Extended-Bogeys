package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.CustomCarriageBogey;
import com.simibubi.create.content.logistics.trains.DimensionPalette;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.TravellingPoint;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(CarriageBogey.class)
public abstract class MixinCarriageBogey {
    @Shadow private IBogeyBlock type;

    @Shadow @Nullable public abstract Vec3 getAnchorPosition();

    @Inject(at = @At("TAIL"), method = "<init>")
    public void CarriageBogey(IBogeyBlock type, TravellingPoint point, TravellingPoint point2, CallbackInfo ci) {
    }

    @Inject(at = @At("RETURN"), method = "read", cancellable = true, remap = false)
    private static void read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions, CallbackInfoReturnable<CarriageBogey> cir) {
        if (tag.contains("Style")) {
            ResourceLocation location = new ResourceLocation(tag.getString("Type"));
            IBogeyBlock type = (IBogeyBlock) ForgeRegistries.BLOCKS.getValue(location);
            Couple<TravellingPoint> points = Couple.deserializeEach(tag.getList("Points", Tag.TAG_COMPOUND),
                    c -> TravellingPoint.read(c, graph, dimensions));
            int style = tag.getInt("Style");
            System.out.println("AB" + style);
            CustomCarriageBogey carriageBogey = new CustomCarriageBogey(type, points.getFirst(), points.getSecond(), style);
            cir.setReturnValue(carriageBogey);
        }
    }
}
