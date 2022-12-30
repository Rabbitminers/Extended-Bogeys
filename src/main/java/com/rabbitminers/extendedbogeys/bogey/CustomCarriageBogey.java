package com.rabbitminers.extendedbogeys.bogey;

import com.simibubi.create.content.logistics.trains.DimensionPalette;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.TravellingPoint;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class CustomCarriageBogey extends CarriageBogey {
    int style;
    public CustomCarriageBogey(IBogeyBlock type, TravellingPoint point, TravellingPoint point2, int style) {
        super(type, point, point2);
        this.style = style;
    }
    @Override
    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = super.write(dimensions);
        System.out.println("Writing " + style);
        tag.putInt("Style", style);
        return tag;
    }

    public static CarriageBogey read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions) {
        ResourceLocation location = new ResourceLocation(tag.getString("Type"));
        IBogeyBlock type = (IBogeyBlock) ForgeRegistries.BLOCKS.getValue(location);
        Couple<TravellingPoint> points = Couple.deserializeEach(tag.getList("Points", Tag.TAG_COMPOUND),
                c -> TravellingPoint.read(c, graph, dimensions));
        int style = tag.getInt("Style");
        CustomCarriageBogey carriageBogey = new CustomCarriageBogey(type, points.getFirst(), points.getSecond(), style);
        return carriageBogey;
    }
}
