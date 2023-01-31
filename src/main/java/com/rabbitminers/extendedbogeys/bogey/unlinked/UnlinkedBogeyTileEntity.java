package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.tileEntity.CachedRenderBBTileEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class UnlinkedBogeyTileEntity extends CachedRenderBBTileEntity {
    private final float wheelRadius = 1;
    LerpedFloat wheelAngle;
    LerpedFloat yaw;
    LerpedFloat pitch;
    public UnlinkedBogeyTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        wheelAngle = LerpedFloat.angular();
        yaw = LerpedFloat.angular();
        pitch = LerpedFloat.angular();
    }

    public float getAngle() {
        return this.wheelAngle.getValue();
    }

    public void updateAngles(CarriageContraptionEntity entity, double distanceMoved) {
        // TODO - TAKE WHEEL RADIUS FROM STYLE
        double angleDiff = 360 * distanceMoved / (Math.PI * 2 * wheelRadius);
        double newWheelAngle = (wheelAngle.getValue() - angleDiff) % 360;
        wheelAngle.setValue(newWheelAngle);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    LerpedFloat virtualAnimation = LerpedFloat.angular();

    public float getVirtualAngle(float partialTicks) {
        return virtualAnimation.getValue(partialTicks);
    }

    public void animate(float distanceMoved) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof IUnlinkedBogeyBlock type))
            return;
        double angleDiff = 360 * distanceMoved / (Math.PI * 2 * type.getWheelRadius());
        double newWheelAngle = (virtualAnimation.getValue() - angleDiff) % 360;
        virtualAnimation.setValue(newWheelAngle);
    }
}
