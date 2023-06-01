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
            wheelAngle = forwards ? wheelAngle : -wheelAngle;
            Transform<?>[] wheels = getTransformsFromPartial(ExtendedBogeysPartials.SMALL_WHEELS.get(color),  ms, inContraption, 2);

            for (int side : Iterate.positiveAndNegative) {
                wheels[(side+1) / 2]
                        .translate(0, 12 / 16f, side + (forwards ? -2 : 2))
                        .rotateX(forwards ? wheelAngle : -wheelAngle);
                finalize(wheels[(side+1) / 2], ms, light, vb);
            }

            Transform<?> frame = getTransformFromPartial(ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_FRAME, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateY(0.2);
            finalize(frame, ms, light, vb);


            Transform<?> pin = getTransformFromPartial(ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_PIN, ms, inContraption)
                    .translateY(0.2);
            finalize(pin, ms, light, vb);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour color) {
            this.createModelInstances(
                materialManager,
                ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_FRAME,
                ExtendedBogeysPartials.SMALL_DOUBLE_AXLE_PIN
            );
            this.createModelInstances(
                materialManager, ExtendedBogeysPartials.SMALL_WHEELS.get(color), 2
            );
        }
    }

    public static class LargeDoubleAxleBogeyRenderer extends ExtendedBogeysBogeyRenderer {

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            wheelAngle = forwards ? wheelAngle : -wheelAngle;
            Transform<?>[] wheels = getTransformsFromPartial(ExtendedBogeysPartials.LARGE_WHEELS.get(color),  ms, inContraption, 2);

            for (int side : Iterate.positiveAndNegative) {
                Transform<?> wheel = wheels[(side+1) / 2];
                wheel.translate(0, 1, side)
                        .rotateX(forwards ? wheelAngle : -wheelAngle);
                finalize(wheel, ms, light, vb);
            }

            Transform<?> connectingRod = getTransformFromPartial(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateX(-wheelAngle);
            finalize(connectingRod, ms, light, vb);

            double linearOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

            Transform<?> drivePin = getTransformFromPartial(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateZ(linearOffset);
            finalize(drivePin, ms, light, vb);

            float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));

            Transform<?> driveRod = getTransformFromPartial(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_ROD, ms, inContraption)
                    .translateZ(forwards ? -0.6 : 0.6)
                    .translateY(0.85)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(offSetScaleFactor * 20 - 10)
                    .translateZ(linearOffset);
            finalize(driveRod, ms, light, vb);

            Transform<?> frame = getTransformFromPartial(ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_FRAMES.get(color), ms, inContraption)
                    .rotateY(forwards ? 0 : 180);
            finalize(frame, ms, light, vb);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour color) {
            this.createModelInstances(
                    materialManager,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_FRAMES.get(color),
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_PIN,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_ROD,
                    ExtendedBogeysPartials.LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD
            );
            this.createModelInstances(
                    materialManager, ExtendedBogeysPartials.LARGE_WHEELS.get(color), 2
            );
        }
    }
}
