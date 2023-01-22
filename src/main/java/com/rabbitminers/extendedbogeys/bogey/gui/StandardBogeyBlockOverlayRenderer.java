package com.rabbitminers.extendedbogeys.bogey.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class StandardBogeyBlockOverlayRenderer {
    public static final IIngameOverlay OVERLAY = StandardBogeyBlockOverlayRenderer::renderOverlay;
    public static int hoverTicks = 0;
    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width,
                                     int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;
    }

}
