package com.rabbitminers.extendedbogeys.mixin.client;

import com.jozufozu.flywheel.api.MaterialManager;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.bogeys.renderers.ExtendedBogeysBogeyRenderer;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.BogeyInstance;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyInstance.class)
public class MixinBogeyInstance {
    @Shadow @Final public BogeyRenderer renderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initialiseContraptionModelData(CarriageBogey bogey, BogeyStyle style, BogeySizes.BogeySize size,
                                               MaterialManager materialManager, CallbackInfo ci) {
        CompoundTag bogeyData = bogey.bogeyData;
        BogeyPaintColour colour = NBTHelper.readEnum(bogeyData, Constants.BOGEY_PAINT_KEY, BogeyPaintColour.class);

        if (renderer instanceof ExtendedBogeysBogeyRenderer fancyRenderer)
            fancyRenderer.initialiseContraptionModelData(materialManager, colour);
    }
}
