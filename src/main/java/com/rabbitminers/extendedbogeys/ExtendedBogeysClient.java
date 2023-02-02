package com.rabbitminers.extendedbogeys;

import com.rabbitminers.extendedbogeys.bogey.gui.StandardBogeyBlockOverlayRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExtendedBogeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ExtendedBogeysClient {
    private ExtendedBogeysClient() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // registerOverlays();
    }

    private static void registerOverlays() {
        OverlayRegistry.registerOverlayAbove(
                ForgeIngameGui.HOTBAR_ELEMENT,
                "Bogey Types",
                StandardBogeyBlockOverlayRenderer.OVERLAY
        );
    }
}
