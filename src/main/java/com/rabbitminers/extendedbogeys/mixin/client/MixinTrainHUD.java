package com.rabbitminers.extendedbogeys.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfig;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.Train;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;
import java.util.Objects;

@Mixin(TrainHUD.class)
public class MixinTrainHUD {
    @Inject(
        method = "renderOverlay",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(
            value="INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
            shift = At.Shift.BEFORE,
            ordinal = 0
        ),
        remap = false
    )
    private static void renderWaterLevels(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width, int height, CallbackInfo ci,
                                          Minecraft mc, CarriageContraptionEntity cce, Carriage carriage, Entity cameraEntity, BlockPos localPos) {
        if (!ExtendedBogeysConfig.SERVER.trainsConsumeWater.get())
            return;

        poseStack.pushPose();

        Train train = carriage.train;
        Font fontRender = mc.font;

        Color color = new Color(120, 200, 255);

        boolean iterateFromBack = train.speed < 0;
        int carriageCount = train.carriages.size();

        int remainingFluids = 0;
        for (int index = 0; index < carriageCount; index++) {
            int i = iterateFromBack ? carriageCount - 1 - index : index;

            Carriage c = train.carriages.get(i);

            IFluidHandler fluidHandler = c.storage.getFluids();

            if (fluidHandler == null)
                continue;

            int tanks = fluidHandler.getTanks();

            for (int j = 0; j < tanks; j++) {
                FluidStack stack = fluidHandler.getFluidInTank(j);

                if (!(stack.getFluid() instanceof WaterFluid)) {
                    continue;
                }
                remainingFluids += 20;
            }
        }

        fontRender.draw(poseStack, "Remaining: " + remainingFluids + "mb",
                10, 10, color.getRGB());
    }
}
