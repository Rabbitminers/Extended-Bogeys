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
import com.simibubi.create.foundation.utility.Iterate;

public class DoubleAxleBogeyRenderer {
    public static class SmallDoubleAxleBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light,
                           VertexConsumer vb, boolean inContraption) {
            BogeyModelData[] wheels = getTransform(ExtendedBogeysPartials.SMALL_WHEELS.get(color),  ms, inContraption, 2);

            for (int side : Iterate.positiveAndNegative) {
                wheels[(side+1) / 2]
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 12 / 16f, side - 2)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);
            }

            this.getTransform(ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_FRAME, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateY(0.2)
                    .render(ms, light, vb);;


            this.getTransform(ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateY(0.2)
                    .render(ms, light, vb);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour color) {
            this.createModelInstance(
                materialManager,
                ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_FRAME,
                ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_PIN
            );

            this.createModelInstance(
                materialManager, ExtendedBogeysPartials.SMALL_WHEELS.get(color), 2
            );
        }
    }

    public static class LargeDoubleAxleBogeyRenderer extends ExtendedBogeysBogeyRenderer {

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            BogeyModelData[] wheels = this.getTransform(ExtendedBogeysPartials.LARGE_WHEELS.get(color),  ms, inContraption, 2);

            for (int side : Iterate.positiveAndNegative) {
                wheels[(side+1) / 2]
                    .translate(0, 1, side)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);
            }

            getTransform(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateX(-wheelAngle)
                    .render(ms, light, vb);

            double linearOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

            getTransform(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateZ(linearOffset)
                    .render(ms, light, vb);

            float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));

            getTransform(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_ROD, ms, inContraption)
                    .translateZ(forwards ? -0.6 : 0.6)
                    .translateY(0.85)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(offSetScaleFactor * 20 - 10)
                    .translateZ(linearOffset)
                    .render(ms, light, vb);

            getTransform(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_FRAMES.get(color), ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .render(ms, light, vb);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour color) {
            this.createModelInstance(
                    materialManager,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_FRAMES.get(color),
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_PIN,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_ROD,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD
            );
            this.createModelInstance(
                    materialManager, ExtendedBogeysPartials.LARGE_WHEELS.get(color), 2
            );
        }
    }
}
