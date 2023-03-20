package com.rabbitminers.extendedbogeys.mixin.client;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyInstance.Drive.class)
public class MixinBogeyDrive {
    private boolean isFacingForward = true;
    private boolean shouldRenderDefault;
    private Direction assemblyDirection;
    private DyeColor paintColour;
    private CarriageBogey bogey;
    IBogeyStyle bogeyStyle;

    /*
    TODO: Bogey Api - Extract Rendering To Seperate Class So It Can Be Modified From Registry
    (Same As In MixinBogeyFrame)
     */

    @OnlyIn(Dist.CLIENT)
    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    public void BogeyInstanceInit(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        this.bogey = bogey;
        int style = 0;
        if (bogey instanceof ICarriageBogeyStyle styledCarriageBogey) {
            style = styledCarriageBogey.getStyle();
            isFacingForward = styledCarriageBogey.isFacingForward();
            assemblyDirection = styledCarriageBogey.getAssemblyDirection();
            paintColour = styledCarriageBogey.getPaintColour();
        }
        bogeyStyle = BogeyStyles.getBogeyStyle(style);
        bogeyStyle.registerBogeyModelData(true, materialManager, paintColour);
        shouldRenderDefault = bogeyStyle.shouldRenderDefault(true);
    }

    @OnlyIn(Dist.CLIENT)
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

            bogeyStyle.renderLargeInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
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
