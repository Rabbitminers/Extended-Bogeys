package com.rabbitminers.extendedbogeys.forge;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtendedBogeys.MOD_ID)
public class ExtendedBogeysImpl {
    public ExtendedBogeysImpl() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ExtendedBogeys.registrate().registerEventListeners(eventBus);
        ExtendedBogeys.init();
    }
}
