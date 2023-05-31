package com.rabbitminers.extendedbogeys.bogeys.renderers;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.Direction;
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
        boolean isForwards = bogeyData.contains(Constants.BOGEY_DIRECTION_KEY) && bogeyData.getBoolean(Constants.BOGEY_DIRECTION_KEY);

        Direction direction = bogeyData.contains(Constants.BOGEY_ASSEMBLY_DIRECTION_KEY)
                ? NBTHelper.readEnum(bogeyData, Constants.BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class)
                : Direction.NORTH;

        return isDirectionPosotive(direction) == isForwards;
    }

    @Nullable
    private BogeyPaintColour getColour(CompoundTag bogeyData) {
        if (!bogeyData.contains(Constants.BOGEY_PAINT_KEY))
            return BogeyPaintColour.UNPAINTED;
        return NBTHelper.readEnum(bogeyData, Constants.BOGEY_PAINT_KEY, BogeyPaintColour.class);
    }

    public static boolean isDirectionPosotive(Direction direction) {
        return switch (direction) { case NORTH, WEST, UP -> true; case SOUTH, DOWN, EAST -> false; };
    }

    @Override
    public final void initialiseContraptionModelData(MaterialManager materialManager) {
        /* Due to a mild fuck up bogey data isn't passed here, this method is empty and is replaced by another until the PR to fix this problem is merged */
    }

    public abstract void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour colour);
}
