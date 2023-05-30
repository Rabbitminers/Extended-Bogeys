package com.rabbitminers.extendedbogeys.bogeys.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogeys.renderers.ExtendedBogeysBogeyRenderer;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.bogey.BogeySizes;

public class SingleAxleBogeyRenderer {
    public static class SmallSingleAxleBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            Transform<?> frame = this.getTransformFromPartial(ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME, ms, inContraption)
                    .rotateY(0)
                    .translate(0, 0.1, 0)
                    .scale(1 - 1/512f);
            finalize(frame, ms, light, vb);

            Transform<?> pin = this.getTransformFromPartial(ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_PIN, ms, inContraption)
                    .rotateY(0)
                    .scale(1 - 1 / 512f);
            finalize(pin, ms, light, vb);

            ms.pushPose();

            Transform<?> wheels = this.getTransformFromPartial(AllPartialModels.SMALL_BOGEY_WHEELS, ms, inContraption)
                    .rotateY(0)
                    .translate(0, 12 / 16f, -1)
                    .rotateX(wheelAngle);
            finalize(wheels, ms, light, vb);

            ms.popPose();
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
            this.createModelInstances(materialManager, ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_PIN);
            this.createModelInstances(materialManager, ExtendedBogeysPartials.SINGLE_AXEL_LEADING_TRUCK_FRAME);
        }
    }
}
