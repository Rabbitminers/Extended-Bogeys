package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyInstance.Drive.class)
public class MixinBogeyDrive {
    private boolean isFacingForward = true;
    private boolean isFrontBogey;
    private boolean shouldRenderDefault;
    IBogeyStyle bogeyStyle;

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    public void BogeyInstanceInit(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        isFrontBogey = (bogey == bogey.carriage.bogeys.get(true));
        int style = 0;
        if (bogey instanceof ICarriageBogeyStyle styledCarriageBogey) {
            style = styledCarriageBogey.getStyle();
            isFacingForward = styledCarriageBogey.isFacingForward();
        }
        bogeyStyle = BogeyStyles.getBogeyStyle(style);
        bogeyStyle.registerBogeyModelData(true, materialManager);
        shouldRenderDefault = bogeyStyle.shouldRenderDefault(true);
    }

    @Inject(
            method = "beginFrame",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/trains/entity/BogeyInstance;beginFrame(FLcom/mojang/blaze3d/vertex/PoseStack;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false,
            cancellable = true
    )
    public void setEmptyTransforms(float wheelAngle, PoseStack ms, CallbackInfo callbackInfo) {
        if (bogeyStyle != null && !shouldRenderDefault) {
            if (ms == null) {
                bogeyStyle.setEmptyTransforms();
                return;
            }

            bogeyStyle.renderLargeInContraption(wheelAngle, isFacingForward, ms);
            callbackInfo.cancel();
        }
    }
    @Inject(
            at = @At("TAIL"),
            method = "updateLight",
            remap = false
    )
    public void updateLight(int blockLight, int skyLight, CallbackInfo ci) {
        bogeyStyle.updateLight(blockLight, skyLight);
    }

    @Inject(at = @At("TAIL"), method = "remove", remap = false)
    public void remove(CallbackInfo ci) {
        bogeyStyle.removeAllModelData();
    }
}
