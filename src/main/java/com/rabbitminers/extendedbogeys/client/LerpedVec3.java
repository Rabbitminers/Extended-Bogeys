package com.rabbitminers.extendedbogeys.client;

import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;

public class LerpedVec3 implements Position {
    public LerpedFloat x;
    public LerpedFloat y;
    public LerpedFloat z;

    LerpedVec3(float x, float y, float z) {
        this.x = LerpedFloat.linear()
                .startWithValue(x);
        this.y = LerpedFloat.linear()
                .startWithValue(y);
        this.z = LerpedFloat.linear()
                .startWithValue(z);
    }

    @Override
    public double x() {
        return this.x.getValue(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public double y() {
        return this.y.getValue(AnimationTickHolder.getPartialTicks());
    }

    @Override
    public double z() {
        return this.z.getValue(AnimationTickHolder.getPartialTicks());
    }

    public void tickChasers() {
        x.tickChaser();
        y.tickChaser();
        z.tickChaser();
    }

    public Vec3 toVec3() {
        return new Vec3(x(), y(), z());
    }

    public void moveTo(BlockPos pos, float time, LerpedFloat.Chaser chaser) {
        x.chase(pos.getX(), time, chaser);
        y.chase(pos.getY(), time, chaser);
        z.chase(pos.getZ(), time, chaser);
    }

    public void moveTo(Vec3 pos, float time, LerpedFloat.Chaser chaser) {
        x.chase(pos.x, time, chaser);
        y.chase(pos.y, time, chaser);
        z.chase(pos.z, time, chaser);
    }

    public double distanceTo(Vec3 pos) {
        return distanceTo(pos, AnimationTickHolder.getPartialTicks());
    }

    public double distanceTo(Vec3 pos, float partialTicks) {
        double d0 = pos.x - x();
        double d1 = pos.y - y();
        double d2 = pos.z - z();
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

}
