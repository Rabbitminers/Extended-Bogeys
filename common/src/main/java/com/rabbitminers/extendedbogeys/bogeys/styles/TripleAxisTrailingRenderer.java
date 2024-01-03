package com.rabbitminers.extendedbogeys.bogeys.styles;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeySizes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import static com.rabbitminers.extendedbogeys.registry.ExtendedBogeysPartials.*;

public class TripleAxisTrailingRenderer {
    public static class MediumTripleAxisTrailingRenderer extends BogeyRenderer {
        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager, CarriageBogey carriageBogey) {
            createModelInstance(materialManager, MEDIUM_SHARED_WHEELS, 3);
            createModelInstance(materialManager, MEDIUM_6_0_6_TRAILING_FRAME);
            createModelInstance(materialManager, AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z), 2);
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return ExtendedBogeysBogeySizes.MEDIUM;
        }

        @Override
        public void render(CompoundTag bogeyData, float wheelAngle, PoseStack ms, int light, VertexConsumer vb, boolean inContraption) {
            BogeyModelData[] secondaryShafts = getTransform(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z), ms, inContraption, 2);

            for (int i : Iterate.zeroAndOne) {
                secondaryShafts[i]
                        .translate(-.5f, .31f, .5f + i * -2)
                        .centre()
                        .rotateZ(wheelAngle)
                        .unCentre()
                        .render(ms, light, vb);
            }

            getTransform(MEDIUM_6_0_6_TRAILING_FRAME, ms, inContraption)
                    .render(ms, light, vb);

            BogeyModelData[] wheels = getTransform(MEDIUM_SHARED_WHEELS, ms, inContraption, 3);
            for (int side = -1; side < 2; side++) {
                if (!inContraption)
                    ms.pushPose();
                BogeyModelData wheel = wheels[side + 1];
                wheel.translate(0, 13 / 16f, side * 1.5)
                        .rotateX(wheelAngle)
                        .translate(0, -13 / 16f, 0)
                        .render(ms, light, vb);
                if (!inContraption)
                    ms.popPose();
            }
        }
    }
}
