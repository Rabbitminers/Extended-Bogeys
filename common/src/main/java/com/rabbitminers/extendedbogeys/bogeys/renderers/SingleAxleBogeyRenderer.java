package com.rabbitminers.extendedbogeys.bogeys.renderers;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import net.minecraft.nbt.CompoundTag;

public class SingleAxleBogeyRenderer {
    public static class CommonSingleAxleBogeyRender extends BogeyRenderer.CommonRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {

        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {

        }
    }

    public static class SmallSingleAxleBogeyRenderer extends BogeyRenderer {
        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            Transform<?> FRAME = this.getTransformFromPartial(ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME, ms, inContraption)
                    .rotateY(0)
                    .translate(0, 0.1, 0)
                    .scale(1 - 1/512f);
            finalize(FRAME, ms, light, vb);

            Transform<?> PIN = this.getTransformFromPartial(ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_PIN, ms, inContraption)
                    .rotateY(0)
                    .scale(1 - 1 / 512f);
            finalize(PIN, ms, light, vb);

            ms.pushPose();

            Transform<?> WHEELS = this.getTransformFromPartial(AllPartialModels.SMALL_BOGEY_WHEELS, ms, inContraption)
                    .rotateY(0)
                    .translate(0, 12 / 16f, -1)
                    .rotateX(wheelAngle);
            finalize(WHEELS, ms, light, vb);

            ms.popPose();
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
            this.createModelInstances(materialManager, ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_PIN);
            this.createModelInstances(materialManager, ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME);
        }
    }
}
