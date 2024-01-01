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
            BogeyModelData[] wheels = this.getTransform(ExtendedBogeysPartials.LARGE_WHEELS.get(color), ms, inContraption, 3);

            for (int side = -1; side < 2; side++) {
                wheels[side+1]
                    .translate(0, 1, side*1.875)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);
            }

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_FRAMES.get(color), ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .render(ms, light, vb);

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .rotateX(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateX(-wheelAngle)
                    .translateY(-4d/16d)
                    .render(ms, light, vb);

            float offSetScaleFactor =  Math.max(0f, (1f - Math.abs(Math.abs(wheelAngle) - 180f) / 180f));
            double zOffset = 1 / 4f * Math.sin(Math.toRadians(wheelAngle));

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 0.9, -1.875)
                    .rotateX(8.5*offSetScaleFactor)
                    .translateZ(zOffset)
                    .render(ms, light, vb);

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_PIN, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translateZ(zOffset)
                    .render(ms, light, vb);

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .translate(0, 0.8, 1.68)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD, ms, inContraption)
                    .rotateY(forwards ? 0 : 180)
                    .render(ms, light, vb);

            this.getTransform(ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD, ms, inContraption)
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
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_FRAMES.get(color),
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_PIN,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC,
                    ExtendedBogeysPartials.LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD
            );

            this.createModelInstance(
                    materialManager, ExtendedBogeysPartials.LARGE_WHEELS.get(color), 3
            );
        }
    }
}
