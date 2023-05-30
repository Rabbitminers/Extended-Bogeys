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

public class TripleAxleBogeyRenderer {
    public static class LargeTripleAxleBogeyRenderer extends ExtendedBogeysBogeyRenderer {

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            double offset = 3d / 16d;

            Transform<?>[] wheels = this.getTransformsFromPartial(AllPartialModels.LARGE_BOGEY_WHEELS, ms, inContraption, 3);
            for (int side = -1; side < 2; side++) {
                Transform<?> wheel = wheels[side + 1];
                wheel.translate(0, 1, side * 1.875).rotateX(wheelAngle);
                finalize(wheel, ms, light, vb);
            }

            ms.translate(0, offset, forwards ? offset : -offset);

            Transform<?> frame = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_FRAME, ms, inContraption)
                    .rotateY(forwards ? 0 : 180);
            finalize(frame, ms, light, vb);

            Transform<?> connectingRod = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateX(-wheelAngle)
                    .translateY(-4d/16d);
            finalize(connectingRod, ms, light, vb);

            float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));
            double zOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

            Transform<?> driveRod = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 0.9, -1.875)
                    .rotateX(8.5*offSetScaleFactor)
                    .translateZ(zOffset);
            finalize(driveRod, ms, light, vb);

            Transform<?> drivePin = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateZ(zOffset);
            finalize(drivePin, ms, light, vb);

            Transform<?> eccentric = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 0.8, 1.68)
                    .rotateX(wheelAngle);
            finalize(eccentric, ms, light, vb);

            Transform<?> eccentricRod = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180);
            finalize(eccentricRod, ms, light, vb);

            Transform<?> radiusRod = this.getTransformFromPartial(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180);
            finalize(eccentricRod, ms, light, vb);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
            this.createModelInstances(
                    materialManager,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_FRAME,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_PIN,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD
            );

            this.createModelInstances(
                    materialManager, AllPartialModels.LARGE_BOGEY_WHEELS, 3
            );
        }
    }
}
