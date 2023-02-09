package com.rabbitminers.extendedbogeys.bogey.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.List;

public class StandardBogeyBlockOverlayRenderer {
    public static final IIngameOverlay OVERLAY = StandardBogeyBlockOverlayRenderer::renderOverlay;
    public static BlockPos lastHovered = null;
    public static int hoverTicks = 0;
    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width,
                                     int height) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR || player == null)
            return;

        HitResult objectMouseOver = mc.hitResult;
        if (!(objectMouseOver instanceof BlockHitResult result)) {
            lastHovered = null;
            hoverTicks = 0;
            return;
        }

        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();

        int prevHoverTicks = hoverTicks;
        if (lastHovered == null || lastHovered.equals(pos))
            hoverTicks++;
        else
            hoverTicks = 0;
        lastHovered = pos;

        poseStack.pushPose();

        for (int i = 0; i < BogeyStyles.getNumberOfBogeyStyleVariations(); i++) {
            IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(i);
            List<GuiGameElement.GuiRenderBuilder> renderComponents =
                    bogeyStyle.renderLargeInGuiOverlay();
            for (GuiGameElement.GuiRenderBuilder component : renderComponents) {
                component.rotate(45, 45, 45).scale(10).at(20 + (18*i) + 12, 20, 450).render(poseStack);
            }
        }
        poseStack.popPose();
     }

}
