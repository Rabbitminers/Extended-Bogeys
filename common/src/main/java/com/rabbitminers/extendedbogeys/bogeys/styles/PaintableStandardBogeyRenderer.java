package com.rabbitminers.extendedbogeys.bogeys.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogeys.renderers.ExtendedBogeysBogeyRenderer;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;

import static com.simibubi.create.AllPartialModels.BOGEY_PIN;
import static com.simibubi.create.AllPartialModels.BOGEY_PISTON;

public class PaintableStandardBogeyRenderer {
    public static class SmallStandardBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour colour) {
            createModelInstance(materialManager, ExtendedBogeysPartials.SMALL_WHEELS.get(colour), 2);
            createModelInstance(materialManager, ExtendedBogeysPartials.BOGEY_FRAMES.get(colour));
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
             getTransform(ExtendedBogeysPartials.BOGEY_FRAMES.get(color), ms, inContraption).render(ms, light, vb);

            BogeyModelData[] wheels = getTransform(ExtendedBogeysPartials.SMALL_WHEELS.get(color), ms, inContraption, 2);

            for (int side : Iterate.positiveAndNegative) {
                wheels[(side + 1) / 2]
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .render(ms, light, vb);
            }
        }
    }

    public static class LargeStandardBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour colour) {
            createModelInstance(materialManager, ExtendedBogeysPartials.LARGE_WHEELS.get(colour),
                    ExtendedBogeysPartials.BOGEY_DRIVES.get(colour), BOGEY_PISTON, BOGEY_PIN);
            createModelInstance(materialManager, AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X), 2);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            BogeyModelData[] secondaryShafts = getTransform(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X), ms, inContraption, 2);

            for (int i : Iterate.zeroAndOne) {
                secondaryShafts[i]
                    .translate(-.5f, .25f, .5f + i * -2)
                    .centre()
                    .rotateX(wheelAngle)
                    .unCentre()
                    .render(ms, light, vb);
            }

            getTransform(ExtendedBogeysPartials.BOGEY_DRIVES.get(color), ms, inContraption).render(ms, light, vb);

            double pistonOffset = 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle));

            getTransform(BOGEY_PISTON, ms, inContraption)
                .translate(0, 0, pistonOffset)
                .render(ms, light, vb);

            getTransform(ExtendedBogeysPartials.LARGE_WHEELS.get(color), ms, inContraption)
                .translate(0, 1, 0)
                .rotateX(wheelAngle)
                .render(ms, light, vb);

            getTransform(BOGEY_PIN, ms, inContraption)
                .translate(0, 1, 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .render(ms, light, vb);
        }
    }
}
