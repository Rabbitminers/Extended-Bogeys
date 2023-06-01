package com.rabbitminers.extendedbogeys.bogeys.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.Transform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.bogeys.renderers.ExtendedBogeysBogeyRenderer;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import static com.simibubi.create.AllPartialModels.BOGEY_PIN;
import static com.simibubi.create.AllPartialModels.BOGEY_PISTON;

public class PaintableStandardBogeyRenderer {
    public static class CommonStandardBogeyRenderer extends BogeyRenderer.CommonRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
            createModelInstances(materialManager, AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z), 2);
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;
            Transform<?>[] shafts = getTransformsFromBlockState(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z), ms, inInstancedContraption, 2);
            for (int i : Iterate.zeroAndOne) {
                shafts[i].translate(-.5f, .25f, i * -1)
                        .centre()
                        .rotateZ(wheelAngle)
                        .unCentre();
                finalize(shafts[i], ms, light, vb);
            }
        }
    }


    public static class SmallStandardBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour colour) {
            createModelInstances(materialManager, ExtendedBogeysPartials.SMALL_WHEELS.get(colour), 2);
            createModelInstances(materialManager, ExtendedBogeysPartials.BOGEY_FRAMES.get(colour));
        }


        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;
            Transform<?> transform = getTransformFromPartial(ExtendedBogeysPartials.BOGEY_FRAMES.get(color), ms, inInstancedContraption);
            finalize(transform, ms, light, vb);

            Transform<?>[] wheels = getTransformsFromPartial(ExtendedBogeysPartials.SMALL_WHEELS.get(color), ms, inInstancedContraption, 2);
            for (int side : Iterate.positiveAndNegative) {
                if (!inInstancedContraption)
                    ms.pushPose();
                Transform<?> wheel = wheels[(side + 1) / 2];
                wheel.translate(0, 12 / 16f, side)
                        .rotateX(wheelAngle);
                finalize(wheel, ms, light, vb);
                if (!inInstancedContraption)
                    ms.popPose();
            }
        }
    }

    public static class LargeStandardBogeyRenderer extends ExtendedBogeysBogeyRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, BogeyPaintColour colour) {
            createModelInstances(materialManager, ExtendedBogeysPartials.LARGE_WHEELS.get(colour),
                    ExtendedBogeysPartials.BOGEY_DRIVES.get(colour), BOGEY_PISTON, BOGEY_PIN);
            createModelInstances(materialManager, AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X), 2);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.LARGE;
        }

        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            boolean inInstancedContraption = vb == null;

            Transform<?>[] secondaryShafts = getTransformsFromBlockState(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.X), ms, inInstancedContraption, 2);

            for (int i : Iterate.zeroAndOne) {
                Transform<?> secondShaft = secondaryShafts[i];
                secondShaft.translate(-.5f, .25f, .5f + i * -2)
                        .centre()
                        .rotateX(wheelAngle)
                        .unCentre();
                finalize(secondShaft, ms, light, vb);
            }

            Transform<?> bogeyDrive = getTransformFromPartial(ExtendedBogeysPartials.BOGEY_DRIVES.get(color), ms, inInstancedContraption);
            finalize(bogeyDrive, ms, light, vb);

            Transform<?> bogeyPiston = getTransformFromPartial(BOGEY_PISTON, ms, inInstancedContraption)
                    .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)));
            finalize(bogeyPiston, ms, light, vb);

            if (!inInstancedContraption)
                ms.pushPose();

            Transform<?> bogeyWheels = getTransformFromPartial(ExtendedBogeysPartials.LARGE_WHEELS.get(color), ms, inInstancedContraption)
                    .translate(0, 1, 0)
                    .rotateX(wheelAngle);
            finalize(bogeyWheels, ms, light, vb);

            Transform<?> bogeyPin = getTransformFromPartial(BOGEY_PIN, ms, inInstancedContraption)
                    .translate(0, 1, 0)
                    .rotateX(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateX(-wheelAngle);
            finalize(bogeyPin, ms, light, vb);

            if (!inInstancedContraption)
                ms.popPose();
        }
    }
}
