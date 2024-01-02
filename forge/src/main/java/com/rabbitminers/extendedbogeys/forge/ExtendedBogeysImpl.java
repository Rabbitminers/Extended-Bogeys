package com.rabbitminers.extendedbogeys.forge;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.ExtendedBogeysClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtendedBogeys.MOD_ID)
public class ExtendedBogeysImpl {
    public ExtendedBogeysImpl() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ExtendedBogeys.registrate().registerEventListeners(eventBus);
        ExtendedBogeys.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> ExtendedBogeysClient::init);
    }
}
