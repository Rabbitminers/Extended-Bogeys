package com.rabbitminers.extendedbogeys.forge;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.ExtendedBogeysClient;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(ExtendedBogeys.MOD_ID)
public class ExtendedBogeysImpl {
    public ExtendedBogeysImpl() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ExtendedBogeys.registrate().registerEventListeners(eventBus);
        ExtendedBogeys.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> ExtendedBogeysClient::init);

        eventBus.addListener(EventPriority.LOWEST, ExtendedBogeysImpl::gatherData);
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExtendedBogeys.gatherData(gen);
    }
}
