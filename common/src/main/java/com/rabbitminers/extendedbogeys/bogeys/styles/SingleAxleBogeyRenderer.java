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
            getTransform(ExtendedBogeysPartials.SMALL_SINGLE_AXLE_FRAME, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 0.1, 0)
                    .scale(1 - 1/512f)
                    .render(ms, light, vb);

            getTransform(ExtendedBogeysPartials.SMALL_SINGLE_AXLE_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .scale(1 - 1 / 512f)
                    .render(ms, light, vb);

            getTransform(ExtendedBogeysPartials.SMALL_WHEELS.get(color), ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 12 / 16f, -1)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour color) {
            this.createModelInstance(materialManager, ExtendedBogeysPartials.SMALL_SINGLE_AXLE_PIN);
            this.createModelInstance(materialManager, ExtendedBogeysPartials.SMALL_SINGLE_AXLE_FRAME);
            this.createModelInstance(materialManager, ExtendedBogeysPartials.SMALL_WHEELS.get(color));
        }
    }
}
