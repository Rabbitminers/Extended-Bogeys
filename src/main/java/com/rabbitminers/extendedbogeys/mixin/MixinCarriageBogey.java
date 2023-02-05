package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.DimensionPalette;
import com.simibubi.create.content.logistics.trains.IBogeyBlock;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.TravellingPoint;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CarriageBogey.class)
public class MixinCarriageBogey implements ICarriageBogeyStyle {
    @Shadow private IBogeyBlock type;

    Direction assemblyDirection = Direction.NORTH;

    private DyeColor paintColour;
    private int style = 0;
    private boolean isFacingForward = true;
    @Override
    public void setStyle(int style) {
        this.style = style;
    }
    @Override
    public int getStyle() {
        return style;
    }

    @Override
    public void setFacingForward(boolean isFacingForward) {
        this.isFacingForward = isFacingForward;
    }
    @Override
    public boolean isFacingForward() {
        return isFacingForward;
    }

    @Override
    public Direction getAssemblyDirection() {
        return assemblyDirection;
    }

    @Override
    public void setAssemblyDirection(Direction assemblyDirection) {
        this.assemblyDirection = assemblyDirection;
    }

    @Override
    public DyeColor getPaintColour() {
        return paintColour;
    }

    @Override
    public void setPaintColour(DyeColor colour) {
        this.paintColour = colour;
    }

    @Inject(at = @At("RETURN"), method = "read", cancellable = true, remap = false)
    private static void read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions, CallbackInfoReturnable<CarriageBogey> cir) {
        if (tag.contains("Style")) {
            ResourceLocation location = new ResourceLocation(tag.getString("Type"));
            IBogeyBlock type = (IBogeyBlock) ForgeRegistries.BLOCKS.getValue(location);
            Couple<TravellingPoint> points = Couple.deserializeEach(tag.getList("Points", Tag.TAG_COMPOUND),
                    c -> TravellingPoint.read(c, graph, dimensions));
            boolean isFacingForward = tag.getBoolean("IsFacingForward");
            DyeColor dyeColor = NBTHelper.readEnum(tag, "PaintColour", DyeColor.class);
            int style = tag.getInt("Style");
            Direction assemblyDirection = NBTHelper.readEnum(tag, "AssemblyDirection", Direction.class);
            CarriageBogey carriageBogey = cir.getReturnValue();
            if (!(carriageBogey instanceof ICarriageBogeyStyle styledCarriageBogey))
                return;
            styledCarriageBogey.setAssemblyDirection(assemblyDirection);
            styledCarriageBogey.setStyle(style);
            styledCarriageBogey.setFacingForward(isFacingForward);
            styledCarriageBogey.setPaintColour(dyeColor);
            cir.setReturnValue((CarriageBogey) styledCarriageBogey);
        }
    }
    @Inject(at = @At("RETURN"), method = "write", remap = false, cancellable = true)
    public void write(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        tag.putInt("Style", style);
        tag.putBoolean("IsFacingForward", isFacingForward);
        NBTHelper.writeEnum(tag, "PaintColour", paintColour);
        NBTHelper.writeEnum(tag, "AssemblyDirection", assemblyDirection);
        cir.setReturnValue(tag);
    }
}
