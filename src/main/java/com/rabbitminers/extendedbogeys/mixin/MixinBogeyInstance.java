package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialGroup;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.RenderLayer;
import com.jozufozu.flywheel.backend.instancing.InstanceManager;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.backend.instancing.batching.BatchingEngine;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BogeyInstance.Frame.class)
public class MixinBogeyInstance {
    @Mutable
    @Shadow @Final private ModelData frame;
    @Mutable
    @Shadow @Final private ModelData[] wheels;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void Frame(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        frame = materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.BLAZE_ACTIVE)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(AllBlockPartials.BLAZE_BURNER_FLAME)
                .createInstances(wheels);
    }
}
