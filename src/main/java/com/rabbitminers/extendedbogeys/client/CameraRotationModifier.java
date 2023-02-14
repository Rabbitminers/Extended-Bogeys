package com.rabbitminers.extendedbogeys.client;

import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.world.phys.Vec2;

public class CameraRotationModifier {
    private static final LerpedFloat xRot = LerpedFloat.angular().startWithValue(0);
    private static final LerpedFloat yRot = LerpedFloat.angular().startWithValue(0);
    private static boolean shouldApply = false;

    public static float getXRotation() {
        return getXRotation(AnimationTickHolder.getPartialTicks());
    }

    public static float getXRotation(float partialTicks) {
        return xRot.getValue(partialTicks);
    }

    public static float getYRotation() {
        return getYRotation(AnimationTickHolder.getPartialTicks());
    }

    public static float getYRotation(float partialTicks) {
        return yRot.getValue(partialTicks);
    }

    public static void tick() {
        xRot.tickChaser();
    }

    public static void reset() {
        xRot.chase(0, 0.1, LerpedFloat.Chaser.EXP);
        yRot.chase(0, 0.1, LerpedFloat.Chaser.EXP);
        shouldApply = false;
    }

    public static void rotateTowards(Vec2 rotationVector) {
        rotateTowards(rotationVector.x, rotationVector.y);
    }

    public static void rotateTowards(float x, float y) {
        xRot.chase(x, 0.075, LerpedFloat.Chaser.EXP);
        yRot.chase(y, 0.075, LerpedFloat.Chaser.EXP);
    }


    public static boolean shouldApply() {
        return shouldApply;
    }

    public static void setShouldApply(boolean shouldApply) {
        CameraRotationModifier.shouldApply = shouldApply;
    }
}
