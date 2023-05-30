package com.rabbitminers.extendedbogeys.bogeys.renderers;

import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public abstract class ExtendedBogeysBogeyRenderer extends BogeyRenderer {
    public static <B extends Transform<?>> void finalize(B b, CompoundTag bogeyData, PoseStack ms, int light,
            @Nullable VertexConsumer vb) {
        boolean isFacingForward =
                bogeyData.contains("direction") && bogeyData.getBoolean("direction");
        b.scale(1 - 1/512f)
            .rotateY(isFacingForward ? 180 : 0);
        if (b instanceof SuperByteBuffer byteBuf && vb != null)
            byteBuf.light(light).renderInto(ms, vb);
    }

    public abstract void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light,
                                VertexConsumer vb, boolean inContraption);

    @Override
    public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb,
           boolean inContraption) {
        this.render(this.isForwards(bogeyData), this.getColour(bogeyData), wheelAngle, ms, light, vb, inContraption);
    }

    private boolean isForwards(CompoundTag bogeyData) {
        if (!bogeyData.contains("Forwards"))
            return false;
        return bogeyData.getBoolean("Forwards");
    }

    @Nullable
    private BogeyPaintColour getColour(CompoundTag bogeyData) {
        if (!bogeyData.contains("Colour"))
            return BogeyPaintColour.UNPAINTED;
        return NBTHelper.readEnum(bogeyData, "Colour", BogeyPaintColour.class);
    }
}
