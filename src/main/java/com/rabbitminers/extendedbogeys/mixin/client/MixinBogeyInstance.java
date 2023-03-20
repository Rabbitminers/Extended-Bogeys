package com.rabbitminers.extendedbogeys.mixin.client;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyInstance.class)
public class MixinBogeyInstance {
    @Shadow @Final private ModelData[] shafts;
    private IBogeyStyle style;

    /*
    TODO: Bogey Api
    Allow For Inner Shaft To Be Disabled By Bogey Renderer
     */

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "<init>", at=@At("TAIL"), remap = false)
    public void onInit(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        int styleId = ((ICarriageBogeyStyle) bogey).getStyle();
        style = BogeyStyles.getBogeyStyle(styleId);
    }
    @OnlyIn(Dist.CLIENT)
    @Inject(method = "beginFrame", at=@At("HEAD"), cancellable = true, remap = false)
    public void beginFrame(float wheelAngle, PoseStack ms, CallbackInfo cir) {
        if (!style.shouldRenderInnerShaft())
            cir.cancel();
    }
}
