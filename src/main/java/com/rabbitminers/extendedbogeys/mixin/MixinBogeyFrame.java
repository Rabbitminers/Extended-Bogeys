package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.GlobalRailwayManager;
import com.simibubi.create.content.logistics.trains.RailwaySavedData;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraption;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BogeyInstance.Frame.class)
public class MixinBogeyFrame {
    @Shadow @Final private ModelData frame;
    private boolean isFacingForward = true;
    private Direction assemblyDirection = Direction.NORTH;
    private CarriageBogey bogey;
    private boolean shouldRenderDefault;
    IBogeyStyle bogeyStyle;

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    public void BogeyInstanceInit(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        this.bogey = bogey;
        int style = 0;
        if (bogey instanceof ICarriageBogeyStyle styledCarriageBogey) {
            style = styledCarriageBogey.getStyle();
            isFacingForward = styledCarriageBogey.isFacingForward();
            assemblyDirection = styledCarriageBogey.getAssemblyDirection();
        }
        bogeyStyle = BogeyStyles.getBogeyStyle(style);
        bogeyStyle.registerBogeyModelData(false, materialManager);
        shouldRenderDefault = bogeyStyle.shouldRenderDefault(false);
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

            bogeyStyle.renderSmallInContraption(wheelAngle, isFacingForward, ms, assemblyDirection);
            callbackInfo.cancel();
        }
    }
    @Inject(at = @At("TAIL"), method = "updateLight", remap = false)
    public void updateLight(int blockLight, int skyLight, CallbackInfo ci) {
        bogeyStyle.updateLight(blockLight, skyLight);
    }

    @Inject(at = @At("TAIL"), method = "remove", remap = false)
    public void remove(CallbackInfo ci) {
        bogeyStyle.removeAllModelData();
    }
}
