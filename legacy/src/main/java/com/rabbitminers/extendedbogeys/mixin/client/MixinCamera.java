package com.rabbitminers.extendedbogeys.mixin.client;

import com.rabbitminers.extendedbogeys.client.CameraRotationModifier;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

@Mixin(Camera.class)
public class MixinCamera {

    /*
    @ModifyArgs(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setRotation(FF)V"
            )
    )

     */

    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setRotation(FF)V"
            ),
            index = 0
    )
    public float modifyCameraRotationX(float oldXRot) {
        if (CameraRotationModifier.shouldApply())
            return CameraRotationModifier.getXRotation();
        return oldXRot;
    }

    @ModifyArg(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setRotation(FF)V"
            ),
            index = 1
    )
    public float modifyCameraRotationY(float oldYRot) {
        if (CameraRotationModifier.shouldApply())
            return CameraRotationModifier.getYRotation();
        return oldYRot;
    }

}
